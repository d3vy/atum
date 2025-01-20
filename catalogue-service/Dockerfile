# Используем официальный образ OpenJDK
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию для приложения
WORKDIR /app/shop-service

# Копируем jar-файл в контейнер
COPY target/catalogue-service-25.1.1-SNAPSHOT-exec.jar catalogue-service.jar

# Устанавливаем переменные окружения
ENV APP_PORT=8081

# Открываем порт, который использует ваше приложение
EXPOSE ${APP_PORT}

# Команда для запуска приложения
CMD ["java", "-jar", "catalogue-service.jar"]
