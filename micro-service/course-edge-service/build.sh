#!/usr/bin/env bash
mvn clean package

docker build -t course-edge-service:latest .