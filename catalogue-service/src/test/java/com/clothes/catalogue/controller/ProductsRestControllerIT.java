package com.clothes.catalogue.controller;

import com.clothes.catalogue.service.general.ProductsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// Сейчас тесты работают с настоящей БД, а не с тестовой базой данных. Необходимо исправить.
@Transactional
@SpringBootTest
@ActiveProfiles("standalone")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class ProductsRestControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Sql("/sql/products.sql")
    void findAllProducts_ReturnsProductsList() throws Exception {
        // Given
        var requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/catalogue/products")
                .param("filter", "Iphone")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")
                ));

        // When
        mockMvc.perform(requestBuilder)

                // Then
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                    {"id": 2, "title": "Iphone 16 pro max", "description": "New iphone with titanium body"},
                                    {"id": 1, "title": "Iphone 16 pro max", "description": "New iphone with titanium body"}
                                ]""")
                );
    }

    @Test
    void findAllProducts_WithoutAuth_ReturnsUnauthorized() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/catalogue/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findAllProducts_WithNoResults_ReturnsEmptyList() throws Exception {
        // Given
        var requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/catalogue/products")
                .param("filter", "NonExisting")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")
                ));

        // When
        mockMvc.perform(requestBuilder)

                // Then
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.length()").value(0)
                );
    }

    @Test
    void createProducts_RequestIsValid_ReturnsNewProduct() throws Exception {
        // Given
        var requestBuilder = MockMvcRequestBuilders.post("/api/v1/catalogue/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "New products", "description": "New product description"}
                        """)
                .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));

        // When
        this.mockMvc.perform(requestBuilder)

                // Then
                .andExpectAll(
                        status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "http://localhost/api/v1/catalogue/products/11"),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "id": 11,
                                    "title": "New products",
                                    "description": "New product description"
                                }
                                """)
                );
    }
}


