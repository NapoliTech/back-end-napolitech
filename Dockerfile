FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/backend-pizzaria-1.0.0.jar backend-pizzaria-1.0.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "backend-pizzaria-1.0.0.jar"]