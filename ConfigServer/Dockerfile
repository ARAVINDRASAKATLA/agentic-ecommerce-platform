# Multi-stage Dockerfile for ConfigServer (Java 17)
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder
WORKDIR /build

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN apk add --no-cache git && \
    addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
