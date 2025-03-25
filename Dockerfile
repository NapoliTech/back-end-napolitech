# Use the official OpenJDK image from the Docker Hub
FROM openjdk:21-jdk-slim

# Set environment variables
ENV JAVA_OPTS=""

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/*.jar app.jar

# Install netcat for waiting for MySQL
RUN apt-get update && apt-get install -y netcat && rm -rf /var/lib/apt/lists/*

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["sh", "-c", "until nc -z -v -w30 mysql 3306; do echo 'Waiting for MySQL...'; sleep 5; done && java $JAVA_OPTS -jar /app/app.jar"]