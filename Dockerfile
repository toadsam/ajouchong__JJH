FROM openjdk:21-slim

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 20232

ENTRYPOINT ["java", "-jar", "app.jar"]