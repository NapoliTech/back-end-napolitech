# Use the official OpenJDK image from the Docker Hub
FROM openjdk:21-jdk-slim

# Set environment variables
ENV JAVA_OPTS=""

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]