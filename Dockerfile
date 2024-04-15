
FROM amazoncorretto:17-alpine-jdk
MAINTAINER david
COPY target/demo-0.0.1-SNAPSHOT.jar embarcadero-app.jar
ENTRYPOINT [ "java","-jar","/embarcadero-app.jar"]