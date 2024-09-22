FROM amazoncorretto:17-alpine3.20
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]