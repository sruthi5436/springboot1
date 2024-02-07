FROM openjdk:11
RUN mkdir /demo
COPY target/demo-0.0.1-SNAPSHOT.jar /demo/demo-0.0.1-SNAPSHOT.jar
WORKDIR /demo
ENTRYPOINT ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]