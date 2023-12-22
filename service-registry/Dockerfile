FROM openjdk:17

ARG JAR_FILE=target/*.jar

WORKDIR /app

COPY ${JAR_FILE} service-registry.jar

ENTRYPOINT ["java", "-jar", "service-registry.jar"]

EXPOSE 8761
