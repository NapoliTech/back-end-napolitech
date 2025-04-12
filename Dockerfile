FROM openjdk:21-slim
WORKDIR /app
RUN apt-get update && apt-get install -y maven
COPY . .
RUN mvn clean package
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/backend-pizzaria-1.0.0.jar"]