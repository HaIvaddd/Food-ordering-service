# ==================================
#  Dockerfile для Spring Boot (Gradle)
# ==================================

# --- ЭТАП 1: Сборка приложения ---
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace

# Копируем файлы Gradle
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# !!! ДОБАВЛЕНО: Устанавливаем права на выполнение для gradlew !!!
RUN chmod +x ./gradlew

# Копируем исходный код
COPY src ./src

# Собираем приложение
# (Добавим отладочный вывод перед запуском, чтобы убедиться, что JAR есть)
RUN ls -l gradle/wrapper && ./gradlew --no-daemon bootJar -x test

# --- ЭТАП 2: Создание финального легковесного образа ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /workspace/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]