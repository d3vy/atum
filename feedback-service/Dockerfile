# Используем официальный образ OpenJDK
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию для приложения
WORKDIR /app/feedback-service

# Копируем jar-файл в контейнер
COPY target/feedback-service-25.1.1-SNAPSHOT-exec.jar feedback-service.jar

# Устанавливаем переменные окружения
ENV APP_PORT=8083

EXPOSE ${APP_PORT}


# Команда для запуска приложения
CMD ["java", "-jar", "feedback-service.jar"]
