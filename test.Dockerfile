# the first stage of our build will use a maven 3.8.6 image
FROM maven:3.8.6-jdk-11 AS base
# copy the pom and src code to the container
COPY pom.xml .
COPY src/ ./src/
# verify the code with tests
CMD mvn clean verify -e -Pwith-tests