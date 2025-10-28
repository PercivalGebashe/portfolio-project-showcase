# ===== Build Stage =====
# Use Maven + JDK for compiling and packaging
FROM maven:3.9.3-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only pom.xml first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the Spring Boot JAR (skip tests to speed up build)
RUN mvn package -DskipTests

# ===== Run Stage =====
# Use lightweight JRE Alpine image for running the app
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose default Spring Boot port
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java","-jar","app.jar"]