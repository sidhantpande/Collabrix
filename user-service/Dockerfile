# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

RUN groupadd --system collabrix \
    && useradd --system --gid collabrix --home-dir /app collabrix \
    && chown -R collabrix:collabrix /app

EXPOSE 8081

USER collabrix

ENTRYPOINT ["java", "-jar", "app.jar"]
