#!/bin/bash
mvn clean package
docker build -t user-thrift-service:latest .