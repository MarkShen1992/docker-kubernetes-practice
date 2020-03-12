#!/bin/bash
mvn clean package
# build harbor
docker build -t hub.image.com/micro-service/user-thrift-service:latest .
# push image to harbor
docker push hub.image.com/micro-service/user-thrift-service:latest