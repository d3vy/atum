package com.clothes.catalogue.controller;

import com.clothes.catalogue.controller.payload.NewProductPayload;
import com.clothes.catalogue.model.Product;
import com.clothes.catalogue.service.general.ProductsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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


    @GetMapping
    @Operation(
            security = @SecurityRequirement(name = "keycloak")
    )
    public Iterable<Product> findAllProducts(
            @RequestParam(name = "filter", required = false) String filter) {
        return this.productsService.findAllProducts(filter);
    }



    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            security = @SecurityRequirement(name = "keycloak"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    type = "object",
                                    properties = {
                                            @StringToClassMapItem(key = "title", value = String.class),
                                            @StringToClassMapItem(key = "description", value = String.class)
                                    }
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            headers = @Header(name = "Content-Type"),
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(
                                                    type = "object",
                                                    properties = {
                                                            @StringToClassMapItem(key = "id", value = Integer.class),
                                                            @StringToClassMapItem(key = "title", value = String.class),
                                                            @StringToClassMapItem(key = "description", value = String.class)
                                                    }
                                            )
                                    )
                            }

                    )
            })
    public ResponseEntity<?> createProduct(@Valid @RequestBody NewProductPayload payload,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        } else {
            Product product = this.productsService.createProduct(payload.title(), payload.description());
            return ResponseEntity.created(
                    uriComponentsBuilder
                            .replacePath("/api/v1/catalogue/products/{productId}")
                            .build(Map.of("productId", product.getId()))
            ).body(product);
        }
    }
}
