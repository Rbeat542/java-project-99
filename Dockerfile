FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .

RUN ./gradlew --no-daemon dependencies

COPY src src
COPY config config

RUN mkdir -p /app/src/main/resources/certs
RUN openssl genpkey -out ./src/main/resources/certs/private.pem -algorithm RSA -pkeyopt rsa_keygen_bits:2048
RUN openssl rsa -in ./src/main/resources/certs/private.pem -pubout -out ./src/main/resources/certs/public.pem

RUN ./gradlew --no-daemon build

EXPOSE 8080

CMD SENTRY_AUTO_INIT=false java -jar build/libs/app-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod --log-level=INFO