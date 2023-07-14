package com.product.service.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.product.service.productservice.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class  ProductServiceApplicationTests {


    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.3");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @DynamicPropertySource
     static void setProperties(DynamicPropertyRegistry registry) {
         registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
     }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(productRequest);
         mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                .contentType("application/json")
                .content(productRequestString))
                .andExpect(status().isCreated()
         );
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("Product 1")
                .description("Product 1 description")
                .price(BigDecimal.valueOf(1200))
                .build();
    }

}
