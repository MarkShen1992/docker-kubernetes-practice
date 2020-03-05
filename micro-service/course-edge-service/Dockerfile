FROM openjdk:8
MAINTAINER MarkShen markshen1992@gmail.com

RUN mkdir /service
WORKDIR /service
COPY target/course-edge-service-0.0.1-SNAPSHOT.jar /service/course-edge-service.jar
RUN cd /service
ENTRYPOINT ["java", "-jar", "course-edge-service.jar"]