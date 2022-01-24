FROM maven:3.6.3-openjdk-11 as build
COPY src usr/src/app/src
COPY pom.xml usr/src/app
RUN mvn -f /usr/src/app/pom.xml -DskipTests clean package

FROM openjdk:11
EXPOSE 80
COPY --from=build /usr/src/app/target/*.jar /app.jar
COPY src/main/resources/ml4fakenews.p12 /ml4fakenews.p12
ENTRYPOINT ["java","-jar","/app.jar"]
