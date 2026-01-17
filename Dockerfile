# Backend Dockerfile 

# Build
FROM eclipse-temurin:11-jdk-alpine AS builder
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B
COPY src src
RUN ./mvnw package -DskipTests -B

# Runtime 
FROM eclipse-temurin:11-jre-alpine AS runtime
RUN apk add --no-cache curl
RUN addgroup -g 1001 -S appgroup && adduser -u 1001 -S appuser -G appgroup
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
RUN chown -R appuser:appgroup /app
USER appuser
EXPOSE 8081
ENV JAVA_OPTS="-Xmx512m -Xms256m" SPRING_PROFILES_ACTIVE="prod" SERVER_PORT="8081"
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 CMD curl -f http://localhost:8081/actuator/health || exit 1
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
