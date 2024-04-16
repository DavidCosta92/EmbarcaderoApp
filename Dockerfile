
FROM amazoncorretto:17-alpine-jdk
COPY target/Embarcadero-app.jar Embarcadero-app.jar
ENTRYPOINT [ "java","-jar","/app-app.jar"]