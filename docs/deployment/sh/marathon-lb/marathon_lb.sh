#!/bin/bash
marathon_ip=192.168.1.105
docker run -p 7000:9090 -e PORTS=9090 mesosphere/marathon-lb:v1.14.1 sse \
  --group marathon \
  --marathon http://$marathon_ip:8080
