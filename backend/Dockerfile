FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -DskipTests
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-alpine
WORKDIR /app
COPY --from=builder /app/target/stock_market.jar .

RUN adduser -D stockmarket_user
USER stockmarket_user

ENTRYPOINT ["java", "-jar", "stock_market.jar"]