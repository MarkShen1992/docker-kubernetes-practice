#!/bin/bash
mvn clean package
docker build -t user-service:latest .