FROM maven:3.9.6-eclipse-temurin-21 AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app
RUN mvn clean install

FROM eclipse-temurin:21-jre-alpine

COPY --from=build /app/target/app.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]