FROM eclipse-temurin:21-jdk-alpine

EXPOSE 8080

VOLUME /tmp

ADD target/payment-service-0.0.1-SNAPSHOT.jar payment-service.jar
#
RUN /bin/sh -c 'touch /payment-service.jar'

ENTRYPOINT ["java","-Xmx256m", "-XX:+UseG1GC", "-Djava.security.egd=file:/dev/./urandom","-jar","/payment-service.jar"]