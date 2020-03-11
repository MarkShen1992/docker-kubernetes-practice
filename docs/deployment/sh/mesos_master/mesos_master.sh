#!/bin/bash
# MESOS_QUORUM=nodes/2 + 1
ip_addr=192.168.1.103
docker run -d --net=host \
  --hostname=$ip_addr \
  -e MESOS_PORT=5050 \
  -e MESOS_ZK=zk://192.168.1.102:2181/mesos \
  -e MESOS_QUORUM=1 \
  -e MESOS_REGISTRY=in_memory \
  -e MESOS_LOG_DIR=/var/log/mesos \
  -e MESOS_WORK_DIR=/var/tmp/mesos \
  -v "$(pwd)/log/mesos:/var/log/mesos" \
  -v "$(pwd)/tmp/mesos:/var/tmp/mesos" \
  mesosphere/mesos-master:1.4.1 \
  --no-hostname_lookup --ip=$ip_addr
