package com.clothes.manager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@ActiveProfiles(value = "standalone")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class ProductsControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "j.daniels", roles = "MANAGER")
    void getNewProductPage_ReturnsProductPage() throws Exception {
        // Given
        var requestBuilder = MockMvcRequestBuilders
                .get("/catalogue/products/create_new_product");

        // When
        this.mockMvc.perform(requestBuilder)

                // Then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("catalogue/products/create_new_product")
                );
    }

}
