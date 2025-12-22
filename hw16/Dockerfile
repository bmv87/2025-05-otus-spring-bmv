# cache deps.
FROM maven:3.9.10-eclipse-temurin-17 AS deps1
WORKDIR /opt/app
COPY /pom.xml .
RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.9.0:go-offline

ARG APP_VERSION="0.0.1"
# build
FROM maven:3.9.10-eclipse-temurin-17 AS builder1
WORKDIR /opt/app
COPY --from=deps1 /root/.m2 /root/.m2
COPY --from=deps1 /opt/app/ /opt/app
COPY /src /opt/app/src
# use -o (--offline) if you need to exclude artifacts.
# if you have excluded artifacts, then remove -o flag
RUN mvn -B -e -o clean install -f pom.xml -DskipTests=true

FROM azul/zulu-openjdk-debian:17-latest AS builder2
ARG JAR_FILE=target/*.jar
COPY --from=builder1 /opt/app/${JAR_FILE} application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# copy app jar to final image
FROM azul/zulu-openjdk-debian:17-latest
WORKDIR /opt/app
COPY --from=builder2 extracted/dependencies/ ./
COPY --from=builder2 extracted/spring-boot-loader/ ./
COPY --from=builder2 extracted/snapshot-dependencies/ ./
COPY --from=builder2 extracted/application/ ./

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]