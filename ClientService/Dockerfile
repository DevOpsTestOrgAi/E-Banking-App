FROM openjdk:17

ARG JAR_FILE=target/*.jar

WORKDIR /app

COPY ${JAR_FILE} ClientService.jar

ENTRYPOINT ["java", "-jar", "ClientService..jar"]

EXPOSE 8087
