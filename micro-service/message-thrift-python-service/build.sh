#!/usr/bin/env bash
# build image
docker build -t hub.image.com/micro-service/message-service:latest .
# push image to harbor
docker push hub.image.com/micro-service/message-service:latest