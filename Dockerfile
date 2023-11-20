# the first stage of our build will use a maven 3.8.6 image
FROM maven:3.8.6-jdk-11 AS base
# copy the pom and src code to the container
COPY pom.xml .
COPY src/ ./src/
# package our application code
RUN mvn clean package

# the second stage of our build will use open jdk 11
FROM openjdk:11-jre-slim
# copy only the artifacts we need from the first stage and discard the rest
COPY --from=base /target/*.jar /app.jar
# set the startup command to execute the jar
CMD ["java", "-jar", "/app.jar"]