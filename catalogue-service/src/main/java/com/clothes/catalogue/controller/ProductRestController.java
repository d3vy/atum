package com.clothes.catalogue.controller;

import com.clothes.catalogue.controller.payload.UpdateProductPayload;
import com.clothes.catalogue.model.Product;
import com.clothes.catalogue.service.general.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/products/{productId:\\d+}")
public class ProductRestController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Product> findProduct(@PathVariable Integer productId) {
        Product product = productService.findProductById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        return ResponseEntity.ok(product);
    }

    @PatchMapping
    public ResponseEntity<Void> updateProduct(@PathVariable Integer productId,
                                              @Valid @RequestBody UpdateProductPayload payload) {
        if (!productService.existsById(productId)) {
            return ResponseEntity.notFound().build(); // Returns 404 Not Found
        }

        productService.updateProduct(productId, payload.title(), payload.description());
        return ResponseEntity.noContent().build(); // Returns 204 No Content
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer productId) {
        if (!productService.existsById(productId)) {
            return ResponseEntity.notFound().build(); // Returns 404 Not Found
        }

        productService.deleteProductById(productId);
        return ResponseEntity.noContent().build(); // Returns 204 No Content
    }
}