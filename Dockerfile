FROM openjdk:17-jdk-slim

WORKDIR /app

EXPOSE 8080

COPY target/transfer-money-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]

