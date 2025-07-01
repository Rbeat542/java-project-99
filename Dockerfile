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

CMD https://0bac6e4ada7d025b87bfa79eb1070d6e@o4509576923906048.ingest.de.sentry.io/4509583732703312 java -jar build/libs/app-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod --log-level=INFO