FROM eclipse-temurin:21-jdk-alpine

EXPOSE 8080

VOLUME /tmp

ADD target/profile-service-0.0.1-SNAPSHOT.jar profile-service.jar
#
RUN /bin/sh -c 'touch /profile-service.jar'

ENTRYPOINT ["java","-Xmx256m", "-XX:+UseG1GC", "-Djava.security.egd=file:/dev/./urandom","-jar","/profile-service.jar"]