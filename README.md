# Simple move spring boot application jar to docker

1. need to manually compile our application

`mvn clean package -DskipTests`

our file should be created:

`target/demo-0.0.1-SNAPSHOT.jar`

2. create Dockerfile

```
FROM openjdk:11
RUN mkdir /demo
COPY target/demo-0.0.1-SNAPSHOT.jar /demo/demo-0.0.1-SNAPSHOT.jar
WORKDIR /demo
ENTRYPOINT ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]
```

3. compile Dockerfile

`docker build -t demo .`

4. run docker 

`docker run -d --name demo -p 8080:8080 demo`

if need override custom port to passed SERVER_PORT

`docker run -d -e SERVER_PORT=8081 -p 8081:8081 demo`

5. check in browser 

`http://localhost:8080`

6. connect to docker

`docker container exec -ti demo /bin/bash`

# Compile src in docker

1. create Dockerfile

```
FROM maven:3.8-jdk-11 AS build_step
RUN mkdir /demo_build
COPY . /demo_build
WORKDIR /demo_build
RUN mvn clean package -DskipTests

FROM adoptopenjdk/openjdk11:jdk-11.0.11_9-alpine-slim
RUN mkdir /demo
COPY --from=build_step /demo_build/target/demo-0.0.1-SNAPSHOT.jar /demo
WORKDIR /demo
CMD ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]
```

2. compile Dockerfile

`docker build -t demo .`

3. run docker 

`docker run -d --name demo -p 8080:8080 demo`

4. check in browser 

`http://localhost:8080`

# Run application with database in docker compose

1. need to manually compile our application

`mvn clean package -DskipTests`

our file should be created:

`target/demo-0.0.1-SNAPSHOT.jar`

2. create Dockerfile

```
FROM openjdk:11
RUN mkdir /demo
COPY target/demo-0.0.1-SNAPSHOT.jar /demo/demo-0.0.1-SNAPSHOT.jar
WORKDIR /demo
ENTRYPOINT ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]
```

3. compile Dockerfile

`docker build -t demo .`

4. create dirs with permission write:
* db
* log

4. Create docker-compose.yml

```
version: "3.1"
services:
  mysql:
    container_name: mysql8
    command: mysqld --default-authentication-plugin=mysql_native_password
    image: mysql:8
    volumes:
      - ./db:/var/lib/mysql
    restart: always
    ports:
      - "3335:3306"
    environment:
      MYSQL_ROOT_PASSWORD: root
    networks:
      - landemo
  demo:
    container_name: demo
    image: demo
    volumes:
      - ./log:/demo/log
    ports:
      - "8086:8081"
    environment:
      SERVER_PORT: 8081
    networks:
      - landemo
networks:
  landemo:
    driver: bridge
```

5. run 

`docker-compose up -d `

6. check existing mysql, connect:

`mysql -uroot -h127.0.0.1 -P3335 -p`

7. check in browser 

`http://localhost:8086`

# Run application with external application.properties

1. Create Dockerfile

```
FROM openjdk:11
RUN mkdir /demo
COPY target/demo-0.0.1-SNAPSHOT.jar /demo/demo-0.0.1-SNAPSHOT.jar
WORKDIR /demo
ENTRYPOINT ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar", "--spring.config.location=resources/application.properties"]
```

2. Create docker-compose.yml
   
```
version: "3.1"
services:
 demo:
   container_name: demo
   image: demo
   volumes:
     - ./log:/demo/log
     - ./resources:/demo/resources
   ports:
     - "8086:8081"
   environment:
     SERVER_PORT: 8081
```