# Используем официальный образ OpenJDK
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию для приложения
WORKDIR /app/customer-application

# Копируем jar-файл в контейнер
COPY target/customer-application-25.1.1-SNAPSHOT-exec.jar customer-application.jar

# Устанавливаем переменные окружения
ENV APP_PORT=8084

EXPOSE ${APP_PORT}


# Команда для запуска приложения
CMD ["java", "-jar", "customer-application.jar"]
