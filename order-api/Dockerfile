# Stage 1: Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the whole project
COPY pom.xml .
COPY order-core/ ./order-core/
COPY order-api/ ./order-api/
COPY order-worker/ ./order-worker/

# Build everything from the root
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy only the JAR built in the previous stage
COPY --from=build /app/order-api/target/*jar order-api.jar

ENTRYPOINT ["java", "-jar", "order-api.jar"]
