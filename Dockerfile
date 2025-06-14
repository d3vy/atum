# Stage 1: Build the application and extract layers
FROM eclipse-temurin:21.0.2_13-jdk-jammy AS build

ARG JAR_FILE
WORKDIR /build

ADD ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract --destination extracted

# Stage 2: Final runtime image
FROM eclipse-temurin:21.0.2_13-jdk-jammy

# Создание пользователя
RUN addgroup spring-boot-group && adduser --ingroup spring-boot-group --disabled-password spring-boot

# Рабочая директория
WORKDIR /application

# Обязательный volume для tmp (Spring Boot)
VOLUME /tmp

# Копируем слои Spring Boot
COPY --from=build /build/extracted/dependencies ./
COPY --from=build /build/extracted/spring-boot-loader ./
COPY --from=build /build/extracted/snapshot-dependencies ./
COPY --from=build /build/extracted/application ./

# Меняем пользователя после копирования (чтобы избежать проблем с правами)
USER spring-boot:spring-boot-group

# Стандартная точка входа Spring Boot
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
