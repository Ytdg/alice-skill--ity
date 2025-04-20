FROM openjdk:17-jdk-slim


WORKDIR /app

# Копируем JAR-файл в контейнер..
COPY /target/*.jar app.jar

# Указываем, что контейнер слушает порт 8080
EXPOSE 8080


ENTRYPOINT ["java", "-jar", "app.jar"]