
FROM amazoncorretto:17-alpine-jdk
MAINTAINER david
COPY ./target/demo-0.0.1-SNAPSHOT.jar ./Embarcadero-app.jar
ENTRYPOINT [ "java","-jar","/Embarcadero-app.jar"]