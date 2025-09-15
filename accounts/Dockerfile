#start with a base image containing Java runtime
FROM openjdk:21-jdk-slim

LABEL "org.opencontainers.image.authors"="yuosefjamal.com"

COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]