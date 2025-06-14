# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set a working directory
WORKDIR /app

# Argument to specify the JAR file name/path
# This allows the JAR file to be passed in during the build process
# or to be easily changed if your build tool produces a differently named JAR.
ARG JAR_FILE=target/*.jar

# Copy the executable JAR file from the target directory (or wherever your build tool puts it)
# to the working directory in the container and rename it to app.jar
COPY ${JAR_FILE} app.jar

# Make port 8080 available to the world outside this container
# This is the default port for Spring Boot.
# If your application runs on a different port internally, change this.
# This can also be overridden by the SERVER_PORT environment variable if your app is configured to use it.
EXPOSE 8080

# Run the JAR file when the container launches
# The array form is preferred for ENTRYPOINT.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Optional: Add healthcheck if needed
# HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 \
#   CMD curl -f http://localhost:8080/actuator/health || exit 1