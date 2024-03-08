FROM maven:3.5-jdk-8 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:8-jre-alpine
EXPOSE 8090
WORKDIR /app
COPY --from=build /usr/src/app/target/test-sibs-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-Dspring.profiles.active=container", "-jar", "test-sibs-0.0.1-SNAPSHOT.jar"]