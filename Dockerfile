FROM maven:3.8-openjdk-17 as builder
WORKDIR /app
COPY . .
RUN mvn clean install

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]