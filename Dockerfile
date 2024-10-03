# Build stage
FROM maven:latest AS BUILD_STAGE_IMAGE
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

#Package stage
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=BUILD_STAGE_IMAGE /app/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
