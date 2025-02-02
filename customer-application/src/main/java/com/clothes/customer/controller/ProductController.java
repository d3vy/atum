package com.clothes.customer.controller;

import com.clothes.customer.client.general.FavoriteProductsClient;
import com.clothes.customer.client.general.ProductReviewsClient;
import com.clothes.customer.client.general.ProductsClient;
import com.clothes.customer.client.payload.NewProductReviewPayload;
import com.clothes.customer.client.exception.ClientBadRequestException;
import com.clothes.customer.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.reactive.result.view.CsrfRequestDataValueProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("customer/products/{productId:\\d+}")
public class ProductController {

    private final ProductsClient productsClient;
    private final FavoriteProductsClient favoriteProductsClient;
    private final ProductReviewsClient productReviewsClient;

    /**
     * Загружает продукт по идентификатору. Если продукт не найден, генерируется ошибка.
     */
    @ModelAttribute(value = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable("productId") Integer productId) {
        return this.productsClient
                .findProduct(productId)
                .switchIfEmpty(Mono.defer(
                        () -> Mono.error(new NoSuchElementException("customer.products.error.not_found"))));
    }

    /**
     * Обрабатывает GET-запрос для страницы продукта.
     * Здесь загружаются:
     * - Избранность продукта для текущего пользователя (inFavorites)
     * - Список отзывов
     * Все данные добавляются в модель перед рендерингом шаблона.
     */
    @GetMapping
    public Mono<String> getProductPage(Model model,
                                       @ModelAttribute("product") Mono<Product> productMono) {
        return productMono.flatMap(product ->
                // Проверяем, находится ли продукт в избранном
                this.favoriteProductsClient.findFavoriteProductByProductId(product.id())
                        // Если найден, маппим в true
                        .map(fav -> true)
                        // Если ответа нет, по умолчанию ставим false
                        .defaultIfEmpty(false)
                        // Добавляем результат в модель
                        .doOnNext(inFavorites -> model.addAttribute("inFavorites", inFavorites))
                        // Далее загружаем отзывы по продукту
                        .then(this.productReviewsClient.findProductReviewsByProductId(product.id())
                                .collectList()
                                .doOnNext(reviews -> model.addAttribute("reviews", reviews)))
                        // Возвращаем имя шаблона для рендеринга страницы
                        .thenReturn("customer/products/product")
        );
    }

    /**
     * Обрабатывает POST-запрос для добавления продукта в избранное.
     * После успешного добавления происходит редирект на страницу продукта.
     */
    @PostMapping("add-to-favorites")
    public Mono<String> addProductToFavorites(@ModelAttribute("product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId ->
                        this.favoriteProductsClient.addProductToFavorites(productId)
                                // После добавления выполняется редирект
                                .thenReturn("redirect:/customer/products/%d".formatted(productId))
                                .onErrorResume(exception -> {
                                    log.error("Ошибка при добавлении в избранное: {}", exception.getMessage(), exception);
                                    return Mono.just("redirect:/customer/products/%d".formatted(productId));
                                })
                );
    }

    /**
     * Обрабатывает POST-запрос для удаления продукта из избранного.
     * После удаления выполняется редирект на страницу продукта.
     */
    @PostMapping("remove-from-favorites")
    public Mono<String> removeProductFromFavorites(@ModelAttribute("product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId ->
                        this.favoriteProductsClient.removeProductFromFavorites(productId)
                                .thenReturn("redirect:/customer/products/%d".formatted(productId))
                );
    }

    /**
     * Обрабатывает POST-запрос для создания отзыва к продукту.
     * В случае ошибок добавляются ошибки и другие данные в модель для повторного отображения формы.
     */
    @PostMapping("create-review")
    public Mono<String> createReview(@ModelAttribute("product") Mono<Product> productMono,
                                     NewProductReviewPayload payload,
                                     Model model,
                                     ServerHttpResponse response) {
        return productMono.flatMap(product ->
                this.productReviewsClient.createProductReview(product.id(), payload.rating(), payload.review())
                        .thenReturn("redirect:/customer/products/%d".formatted(product.id()))
                        .onErrorResume(ClientBadRequestException.class, exception -> {
                            // В случае ошибки устанавливаем inFavorites в false по умолчанию
                            model.addAttribute("inFavorites", false);
                            model.addAttribute("payload", payload);
                            model.addAttribute("errors", exception.getErrors());
                            response.setStatusCode(HttpStatus.BAD_REQUEST);
                            // Обновляем состояние избранного для отображения кнопки
                            return this.favoriteProductsClient.findFavoriteProductByProductId(product.id())
                                    .doOnNext(fav -> model.addAttribute("inFavorites", true))
                                    .thenReturn("customer/products/product");
                        })
        );
    }

    /**
     * Обработчик исключения NoSuchElementException.
     * Устанавливает статус 404 и передаёт сообщение об ошибке в модель.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public String handeNoSuchElementException(Model model,
                                              NoSuchElementException exception,
                                              ServerHttpResponse response) {
        model.addAttribute("error", exception.getMessage());
        response.setStatusCode(HttpStatus.NOT_FOUND);
        return "errors/404";
    }

    /**
     * Загружает CSRF-токен для формы.
     */
    @ModelAttribute
    public Mono<CsrfToken> loadCsrfToken(ServerWebExchange exchange) {
        return Objects.requireNonNull(exchange.<Mono<CsrfToken>>getAttribute(CsrfToken.class.getName()))
                .doOnSuccess(token -> exchange.getAttributes()
                        .put(CsrfRequestDataValueProcessor.DEFAULT_CSRF_ATTR_NAME, token));
    }
}
