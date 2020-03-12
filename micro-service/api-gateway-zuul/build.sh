#!/usr/bin/env bash
mvn clean package
# build image
docker build -t hub.image.com/micro-service/api-gateway-zuul:latest .
# push image to harbor
docker push hub.image.com/micro-service/api-gateway-zuul:latest