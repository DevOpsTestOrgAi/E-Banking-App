FROM openjdk:17

ARG JAR_FILE=target/*.jar

WORKDIR /app

COPY ${JAR_FILE} CloudGateway.jar

ENTRYPOINT ["java", "-jar", "CloudGateway.jar"]

EXPOSE 9296
