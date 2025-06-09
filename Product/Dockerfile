FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY target/*.jar /app/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar


