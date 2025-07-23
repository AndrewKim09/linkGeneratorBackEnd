FROM openjdk:21
ARG JAR_FILE=raget/*.jar
COPY ./target/linkGeneratorBackend.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]