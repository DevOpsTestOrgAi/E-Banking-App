FROM openjdk:17

ARG JAR_FILE=target/*.jar

WORKDIR /app

COPY ${JAR_FILE} TransferService.jar

ENTRYPOINT ["java", "-jar", "TransferService.jar"]

EXPOSE 8082
