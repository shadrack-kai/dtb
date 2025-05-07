FROM eclipse-temurin:21-jdk-alpine

EXPOSE 8080

VOLUME /tmp

ADD target/events-service-0.0.1-SNAPSHOT.jar events-service.jar
#
RUN /bin/sh -c 'touch /events-service.jar'

ENTRYPOINT ["java","-Xmx256m", "-XX:+UseG1GC", "-Djava.security.egd=file:/dev/./urandom","-jar","/events-service.jar"]