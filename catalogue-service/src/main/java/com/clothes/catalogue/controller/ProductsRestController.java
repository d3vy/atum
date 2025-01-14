package com.clothes.catalogue.controller;

import com.clothes.catalogue.controller.payload.NewProductPayload;
import com.clothes.catalogue.model.Product;
import com.clothes.catalogue.service.general.ProductsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/products")
public class ProductsRestController {

    private final ProductsService productsService;

    @GetMapping // Метод для получения всех товаров с учетом фильтра
    public Iterable<Product> findAllProducts(
            @RequestParam(name = "filter", required = false) String filter) {
        return this.productsService.findAllProducts(filter);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProduct(@Valid @RequestBody NewProductPayload payload,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest().body(null); // Returns 400 Bad Request
        } else {
            Product product = this.productsService.createProduct(payload.title(), payload.description());
            return ResponseEntity
                    .created(
                            uriComponentsBuilder
                                    .replacePath("/api/v1/catalogue/products/{productId}")
                                    .build(Map.of("productId", product.getId()))
                    ).body(product); // Returns 201 created
        }
    }
}
