# Use a base image with a Java runtime environment (JRE)
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR file into the container
# Assuming you build your Spring Boot app into a JAR named "football-standings.jar"
COPY target/standing-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that your Spring Boot application listens on (usually 8080)
EXPOSE 8080

# Command to run the Spring Boot application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]