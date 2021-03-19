FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
EXPOSE 8080
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","/app.jar"]