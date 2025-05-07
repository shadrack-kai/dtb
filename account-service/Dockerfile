FROM eclipse-temurin:21-jdk-alpine

EXPOSE 8080

VOLUME /tmp

ADD target/account-service-0.0.1-SNAPSHOT.jar account-service.jar
#
RUN /bin/sh -c 'touch /account-service.jar'

ENTRYPOINT ["java","-Xmx256m", "-XX:+UseG1GC", "-Djava.security.egd=file:/dev/./urandom","-jar","/account-service.jar"]