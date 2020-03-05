FROM openjdk:8
MAINTAINER MarkShen markshen1992@gmail.com

RUN mkdir /service
WORKDIR /service
COPY target/api-gateway-zuul-0.0.1-SNAPSHOT.jar /service/api-gateway-zuul.jar
RUN cd /service
ENTRYPOINT ["java", "-jar", "api-gateway-zuul.jar"]