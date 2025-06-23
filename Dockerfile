FROM eclipse-temurin:21-jdk

RUN apt-get update && apt-get install -y wget unzip make

WORKDIR /demo

COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .

RUN ./gradlew --no-daemon dependencies

COPY src src
COPY config config

RUN ./gradlew --no-daemon build

EXPOSE 8080

CMD java -jar build/libs/demo-0.0.1-SNAPSHOT.jar