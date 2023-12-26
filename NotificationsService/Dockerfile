FROM openjdk:17

ARG JAR_FILE=target/*.jar

WORKDIR /app

COPY ${JAR_FILE} NotificationsService.jar

ENTRYPOINT ["java", "-jar", "NotificationsService.jar"]

EXPOSE 8087
