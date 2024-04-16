
FROM amazoncorretto:17-alpine-jdk
COPY out/artifacts/demo_jar/demo.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]