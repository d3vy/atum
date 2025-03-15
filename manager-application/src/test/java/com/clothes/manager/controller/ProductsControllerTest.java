package com.clothes.manager.controller;

import com.clothes.manager.client.general.ProductsClient;
import com.clothes.manager.controller.payload.NewProductPayload;
import com.clothes.manager.dto.Product;
import com.clothes.manager.exception.error.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductsControllerTest {

    @Mock
    ProductsClient productsClient;

    @InjectMocks
    ProductsController controller;

    @Test
    @DisplayName("createNewProduct: should create a product and redirect to the product page")
    void createNewProduct_RequestIsValid_RedirectsToProductPage() {
        // Given
        var payload = new NewProductPayload("Iphone 16", "Cool thing");
        var model = new ConcurrentModel();

        Mockito.doReturn(new Product(1, "Iphone 16", "Cool thing"))
                .when(productsClient)
                .createProduct("Iphone 16", "Cool thing");

        // When
        var result = this.controller.createNewProduct(payload, model);

        // Then
        Assertions.assertEquals("redirect:/catalogue/products/1", result);

        Mockito.verify(this.productsClient).createProduct("Iphone 16", "Cool thing");
        Mockito.verifyNoMoreInteractions(this.productsClient);
    }

    @Test
    @DisplayName("createNewProduct: should return the new product page with errors")
    void createNewProduct_RequestIsInvalid_ReturnsNewProductPageButWithErrors() {
        // Given
        var payload = new NewProductPayload(" ", null);
        var model = new ConcurrentModel();

        Mockito.doThrow(new BadRequestException(List.of("error 1", "error 2")))
                .when(this.productsClient)
                .createProduct(" ", null);

        // When
        var result = this.controller.createNewProduct(payload, model);

        // Then
        Assertions.assertEquals("catalogue/products/create_new_product", result);
        Assertions.assertEquals(payload, model.getAttribute("payload"));
        Assertions.assertEquals(List.of("error 1", "error 2"), model.getAttribute("errors"));

        Mockito.verify(this.productsClient).createProduct(" ", null);
        Mockito.verifyNoMoreInteractions(this.productsClient);
    }
}