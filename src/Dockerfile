FROM openjdk:13-jdk-alpine3.10
EXPOSE 9797
COPY bibserver-0.0.1-SNAPSHOT.jar bibserver.jar
ENTRYPOINT ["java","-jar","-Djava.security.egd=file:/dev/./urandom", "-Duser.timezone=GMT+08", "/bibserver.jar"]