## Build stage — runs Gradle inside the image so the host doesn't need JDK/Gradle.
FROM gradle:8.14-jdk17 AS builder
WORKDIR /app
COPY settings.gradle build.gradle ./
COPY src ./src
RUN gradle --no-daemon bootJar

## Runtime stage — slim JRE, non-root user.
FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S lodwar && adduser -S lodwar -G lodwar
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
USER lodwar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
