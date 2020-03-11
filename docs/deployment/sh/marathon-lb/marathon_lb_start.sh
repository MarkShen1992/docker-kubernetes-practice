#!/bin/bash
marathon_ip=192.168.1.105
docker run -d --net=host \
  -e PORTS=9090 mesosphere/marathon-lb:v1.14.1 sse \
  --group external \
  --marathon http://$marathon_ip:8080
