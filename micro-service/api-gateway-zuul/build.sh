#!/usr/bin/env bash
mvn clean package

docker build -t api-gateway-zuul:latest .