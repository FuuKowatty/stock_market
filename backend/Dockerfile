ARG source=build_jar

# ${source} option 1
FROM maven:3.9-eclipse-temurin-21 AS build_jar
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -DskipTests;
COPY src ./src
RUN mvn clean package -DskipTests -B;

# ${source} option 2
FROM scratch AS remote_build_jar
WORKDIR /app
COPY ./target/stock_market.jar /app/target/stock_market.jar

FROM ${source} AS source_alias

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=source_alias /app/target/stock_market.jar .

RUN adduser -D stockmarket_user
USER stockmarket_user

ENTRYPOINT ["java", "-jar", "stock_market.jar"]