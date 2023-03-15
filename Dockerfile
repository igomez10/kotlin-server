# Dockerfile for kotlin server
# Build jar in container
FROM openjdk:11-jdk-slim as build
WORKDIR /home/gradle/src
COPY . .
RUN ./gradlew --no-daemon build


# Build image
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/app.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]