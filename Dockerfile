FROM openjdk:17-jdk-slim

WORKDIR /app

COPY ./target/orders-service-0.0.1-SNAPSHOT.jar .

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "orders-service-0.0.1-SNAPSHOT.jar"]
