# Используем официальный образ OpenJDK
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию для приложения
WORKDIR /app/manager-application

# Копируем jar-файл в контейнер
COPY target/manager-application-25.1.1-SNAPSHOT-exec.jar manager-application.jar

# Устанавливаем переменные окружения
ENV APP_PORT=8082

EXPOSE ${APP_PORT}


# Команда для запуска приложения
CMD ["java", "-jar", "manager-application.jar"]
