package com.clothes.manager.exception.handling;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;
import java.util.NoSuchElementException;


@RequiredArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {

    private final MessageSource messageSource;


    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(
            NoSuchElementException exception,
            Model model,
            HttpServletResponse response,
            Locale locale) {
        response.setStatus(HttpStatus.NOT_FOUND.value());

        String errorMessage = this.messageSource.getMessage(
                exception.getMessage(),
                null,
                exception.getMessage(),
                locale
        );

        model.addAttribute("errors", errorMessage);

        return "errors/404";
    }
}
