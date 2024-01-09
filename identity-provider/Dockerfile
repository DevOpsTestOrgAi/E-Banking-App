FROM openjdk:17

ARG JAR_FILE=target/*.jar

WORKDIR /app

COPY ${JAR_FILE} identityproviderService.jar

ENTRYPOINT ["java", "-jar", "identityproviderService.jar"]

EXPOSE 8083
