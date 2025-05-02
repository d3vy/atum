package com.clothes.catalogue.service.impl;

import com.clothes.catalogue.model.Product;
import com.clothes.catalogue.repository.ProductRepository;
import com.clothes.catalogue.service.general.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Реализация сервиса для работы с товарами.
 * Обеспечивает получение, обновление, удаление и проверку существования товаров.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    // Репозиторий для доступа к данным сущности Product
    private final ProductRepository productRepository;

    /**
     * Ищет продукт по идентификатору.
     *
     * @param productId идентификатор продукта
     * @return Optional, содержащий найденный продукт или пустой, если продукт не найден
     */
    @Override
    public Optional<Product> findProductById(Integer productId) {
        log.info("Searching for product with id: {}", productId);
        return this.productRepository.findById(productId);
    }

    /**
     * Обновляет название и описание продукта по его идентификатору.
     * Если продукт не найден, выбрасывается NoSuchElementException.
     *
     * @param id          идентификатор продукта
     * @param title       новое название продукта
     * @param description новое описание продукта
     */
    @Override
    @Transactional
    public void updateProduct(Integer id, String title, String description) {
        this.productRepository.findById(id).ifPresentOrElse(product -> {
                    // Обновление полей продукта
                    product.setTitle(title);
                    product.setDescription(description);
                    log.info("Product with id: {} has been updated", id);
                }, () -> {
                    // Если продукт не найден, логируем ошибку и выбрасываем исключение
                    log.error("Product with id: {} not found or has invalid fields to update", id);
                    throw new NoSuchElementException("Product with id " + id + " not found");
                }
        );
    }

    /**
     * Удаляет продукт по его идентификатору.
     *
     * @param id идентификатор продукта, который необходимо удалить
     */
    @Override
    @Transactional
    public void deleteProductById(Integer id) {
        log.info("Deleting product with id: {}", id);
        this.productRepository.deleteById(id);
    }

    /**
     * Проверяет, существует ли продукт с заданным идентификатором.
     *
     * @param productId идентификатор продукта
     * @return true, если продукт существует, иначе false
     */
    @Override
    public boolean existsById(Integer productId) {
        log.info("Checking if product with id: {} exists", productId);
        return this.productRepository.existsById(productId);
    }

    @Override
    public void save(Product product) {
        this.productRepository.save(product);
    }
}
