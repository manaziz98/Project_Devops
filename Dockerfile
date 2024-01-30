# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
