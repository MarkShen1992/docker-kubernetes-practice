#!/usr/bin/env bash
mvn clean package

docker build -t user-edge-service:latest .