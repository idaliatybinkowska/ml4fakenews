FROM maven:3.6.3-openjdk-11 as build
COPY src usr/src/app/src
COPY pom.xml usr/src/app
RUN mvn -f /usr/src/app/pom.xml -DskipTests clean package

FROM openjdk:11
EXPOSE 3000
COPY --from=build /usr/src/app/target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]