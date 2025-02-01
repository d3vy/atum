package com.clothes.catalogue.exception.handling.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений для всех контроллеров.
 * Перехватывает и обрабатывает различные типы исключений, возвращая структурированные ответы с деталями ошибки.
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalRequestControllerAdvice {

    // Источник сообщений для локализации сообщений об ошибках
    private final MessageSource messageSource;

    /**
     * Обрабатывает исключения валидации (BindException), возникающие при некорректном заполнении полей запроса.
     *
     * @param exception объект исключения, содержащий детали ошибок валидации.
     * @param locale    локаль клиента для локализации сообщения.
     * @return ResponseEntity с объектом ProblemDetail и статусом 400 Bad Request.
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException exception, Locale locale) {
        log.error("Validation error occurred: {}", exception.getMessage());

        // Создаем объект ProblemDetail с кодом ошибки 400 и локализованным сообщением
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                messageSource.getMessage("errors.400.title", null, "errors.400.title", locale)
        );

        // Добавляем в объект ProblemDetail список сообщений об ошибках
        problemDetail.setProperty("errors",
                exception.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.toList())
        );

        return ResponseEntity.badRequest().body(problemDetail);
    }

    /**
     * Обрабатывает исключения, связанные с отсутствием элемента (NoSuchElementException).
     *
     * @param exception объект исключения, содержащее сообщение об ошибке.
     * @param locale    локаль клиента для локализации сообщения.
     * @return ResponseEntity с объектом ProblemDetail и статусом 404 Not Found.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException exception, Locale locale) {
        log.error("No such element: {}", exception.getMessage());

        // Создаем объект ProblemDetail с кодом ошибки 404 и локализованным сообщением
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                messageSource.getMessage("errors.404.title", null, "errors.404.title", locale)
        );

        // Добавляем дополнительное свойство с информацией об ошибке
        problemDetail.setProperty("error", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    /**
     * Обрабатывает исключения, связанные с некорректными аргументами (IllegalArgumentException).
     *
     * @param exception объект исключения, содержащее сообщение об ошибке.
     * @param locale    локаль клиента для локализации сообщения.
     * @return ResponseEntity с объектом ProblemDetail и статусом 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(IllegalArgumentException exception, Locale locale) {
        log.error("Illegal argument: {}", exception.getMessage());

        // Создаем объект ProblemDetail с кодом ошибки 400 и локализованным сообщением
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                messageSource.getMessage("errors.400.title", null, "errors.400.title", locale)
        );

        // Добавляем дополнительное свойство с информацией об ошибке
        problemDetail.setProperty("error", exception.getMessage());

        return ResponseEntity.badRequest().body(problemDetail);
    }

    /**
     * Обрабатывает все остальные необработанные исключения.
     *
     * @param exception объект исключения, содержащее сообщение об ошибке.
     * @param locale    локаль клиента для локализации сообщения (в данном случае не используется).
     * @return ResponseEntity с объектом ProblemDetail и статусом 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception exception, Locale locale) {
        log.error("Unexpected error: {}", exception.getMessage());

        // Создаем объект ProblemDetail с кодом ошибки 500 и сообщением об ошибке
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}
