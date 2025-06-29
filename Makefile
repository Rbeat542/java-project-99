run:
	./gradlew bootRun --args='--spring.profiles.active=development'

clean:
	./gradlew clean

build:
	./gradlew clean build

reload-classes:
	./gradlew -t classes

install:
	./gradlew installDist

makecert:
	mkdir -p ./src/main/resources/certs
	openssl genpkey -out ./src/main/resources/certs/private.pem -algorithm RSA -pkeyopt rsa_keygen_bits:2048
	openssl rsa -in ./src/main/resources/certs/private.pem -pubout -out ./src/main/resources/certs/public.pem

lint:
	./gradlew checkstyleMain checkstyleTest

report:
	./gradlew jacocoTestReport

.PHONY: build