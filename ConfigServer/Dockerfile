FROM openjdk:17

ARG JAR_FILE=target/*.jar

WORKDIR /app

COPY ${JAR_FILE} config-server.jar

ENTRYPOINT ["java", "-jar", "config-server.jar"]

EXPOSE 9296
