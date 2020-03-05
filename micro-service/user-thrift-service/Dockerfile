FROM openjdk:8
MAINTAINER MarkShen markshen1992@gmail.com

RUN mkdir /service
WORKDIR /service
COPY target/user-thrift-service-0.0.1-SNAPSHOT.jar /service/user-service.jar
RUN cd /service
ENTRYPOINT ["java", "-jar", "user-service.jar"]