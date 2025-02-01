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

/**
 * Глобальный обработчик исключений для контроллеров.
 */
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {

    private final MessageSource messageSource;

    /**
     * Обрабатывает исключения NoSuchElementException, возникающие при отсутствии запрашиваемого элемента.
     *
     * @param exception Исключение, которое было выброшено
     * @param model     Модель для передачи данных в представление
     * @param response  HTTP-ответ для установки статуса
     * @param locale    Текущая локаль пользователя
     * @return Имя представления ошибки 404
     */
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(
            NoSuchElementException exception,
            Model model,
            HttpServletResponse response,
            Locale locale) {
        // Устанавливаем HTTP-статус 404 (Не найдено)
        response.setStatus(HttpStatus.NOT_FOUND.value());

        // Получаем сообщение об ошибке из MessageSource с учетом локали
        String errorMessage = this.messageSource.getMessage(
                exception.getMessage(),
                null,
                exception.getMessage(),
                locale
        );

        // Добавляем сообщение об ошибке в модель
        model.addAttribute("errors", errorMessage);

        // Возвращаем имя представления для отображения ошибки 404
        return "errors/404";
    }
}
