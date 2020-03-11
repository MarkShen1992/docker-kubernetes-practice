#!/bin/bash
ip_addr=192.168.1.105
zk_addr=192.168.1.104
docker run -d --net=host \
 mesosphere/marathon:v1.5.2 \
 --hostname=$ip_addr \
 --master zk://$zk_addr:2181/mesos \
 --zk zk://$zk_addr:2181/marathon 
