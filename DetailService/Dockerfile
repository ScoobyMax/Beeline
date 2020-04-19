FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/application-docker.properties application.properties
ENTRYPOINT ["java","-jar","/app.jar"]