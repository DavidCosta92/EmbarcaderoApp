
FROM amazoncorretto:17-alpine-jdk
COPY target/demo-0.0.1-SNAPSHOT.jar Embarcadero-app.jar
ENTRYPOINT [ "java","-jar","/app-app.jar"]