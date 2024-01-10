FROM openjdk:17

ARG JAR_FILE=target/*.jar

WORKDIR /app

COPY ${JAR_FILE} BankTrackingService.jar

ENTRYPOINT ["java", "-jar", "BankTrackingService.jar"]

EXPOSE 8087
