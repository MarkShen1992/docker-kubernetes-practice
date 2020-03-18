# 系统整体架构

![系统整体架构图](https://github.com/MarkShen1992/docker-kubernetes-practice/blob/master/docs/archetecture.png)

# 容器的 Endpoint 配置

### 服务运行环境说明

#### 使用软件

- CentOS 7
- Docker 19.03.2
- Springboot 2.2.4.RELEASE
- zuul 1.5.10.RELEASE
- thrift 0.13.0
- IntelliJ Idea
- MySQL 8.0.19
- Zookeeper
- Redis
- Java 8
- Dubbo 2.7.5
- Moba Xterm (SSH工具)
- Github

#### 服务名称，IP地址等描述

| 服务名称             | IP地址        | 端口号 |
| :------------------- | :------------ | :----- |
| user-thrift-service  | 192.168.1.102 | 7911   |
| user-edge-service    | 192.168.1.102 | 8082   |
| message-service      | 192.168.1.102 | 9090   |
| course-dubbo-service | 192.168.1.102 | 8081   |
| course-edge-service  | 192.168.1.102 | 8083   |
| api-gateway-zuul     | 192.168.1.102 | 8080   |
| **mysql**            | 192.168.1.102 | 3307   |
| **zookeeper**        | 192.168.1.102 | 2181   |
| **redis**            | 192.168.1.102 | 6379   |

> **注意：**
>
> 在使用 Docker 部署 **user-thrift-service, user-edge-service, message-service, course-dubbo-service,** 
>
> **course-edge-service, api-gateway-zuul** 这几个服务的时候，使用 docker-compose 来做程序启动时候的
>
> 配置，api-gateway-zuul 作为整个以上六个服务的入口，唯一开放方端口号。这六个程序之间通讯通过
>
> docker-compose 创建整个服务时候，默认创建的 bridge 网络。进入 api-gateway-zuul 容器内， 执行 
>
> ping user-thrift-service 可以 ping 得通。
>
> 
>
> 另外，**mysql, zookeeper, redis** 这几个作为基础服务(**外围服务**)。在做连接得时候，一定要记得对外开放端
>
> 口，可以参考[文章](https://markshen1992.github.io/document/devops/ops/linux/linux_basic_command.html)。 
>
> 
>
> 这次部署使用虚拟机工具 VMware Workstation 来做部署安装 Linux OS。Linux OS IP 是 192.168.1.102， 
>
> 请换成你自己机器的 IP 地址。

# Docker 容器部署应用程序

## Docker 容器间通讯方案

- 通过容器的 Endpoint 去做
- 服务把端口映射出去，容器端口转换成主机端口 
  - zk, redis, mysql
- 使用Docker link地址的方式，然后通过名字就可以实现 Docker 容器之间的通信了 
  - api-gateway-zuul, message-service etc

### 使用 Docker 的 docker-compose 来实现

使用 `docker compose`去描述服务之间的关系非常好用，当然也可以使用 Docker 命令去建立 link 关系

在使用[Docker compose](https://github.com/docker/compose/releases)前要对其进行安装

```
curl -L https://github.com/docker/compose/releases/download/1.25.4/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
```

编写 `docker-compose.yml`文件，文件见程序中 [micro-service](https://github.com/MarkShen1992/docker-kubernetes-practice/tree/master/micro-service)

docker-compose 使用命令

```
docker-compose up -d --启动服务
docker-compose down --关闭服务
docker-compose up -d service-name --单独启动某个服务
```

使用Portainer来管理Docker，查看[Portainer安装](https://markshen1992.github.io/document/devops/ops/linux/docker_visualize.html)来安装Portainer `Docker 管理工具`。

**Note:** 使用 docker-compose up -d 来启动所有容器的时候，开启程序对应的端口号

- [Linux环境下开启端口号命令](https://markshen1992.github.io/document/devops/ops/linux/linux_basic_command.html)

# 镜像仓库

## 分类

- 共有仓库， 像 [docker hub](https://hub.docker.com)
- 私有仓库，有两种 [registry](https://hub.docker.com/_/registry), [harbor](https://goharbor.io/), [nexus](https://my.sonatype.com/)

## 每种镜像仓库的使用

### 1. docker hub 的使用

- 需要先在 [官网](https://hub.docker.com)上注册账号

  ```
  docker login
  docker push image_name
  docker pull image_name
  ```

### 2. registry 的使用

- registry安装并运行registry:2

  ```
  docker pull registry
  docker run -d -p 5000:5000 --restart always --name registry registry:2
  ```

- 一个完整的教程-把 registry:sjy 上传到 registry 上

  ```
  # step 01 registry安装并运行registry:2
  
  # step 02 registry:2 => localhost:5000/registry:sjy
  docker tag registry:2 localhost:5000/registry:sjy
  
  # step 03 推镜像到 registry:2 仓库
  docker push localhost:5000/registry:sjy
  
  # step 04 pull 到本地
  docker pull localhost:5000/registry:sjy
  ```

# Docker Swarm

![Docker Swarm Arch](https://github.com/MarkShen1992/docker-kubernetes-practice/blob/docker-swarm-env-deploy/docs/Docker%20swarm%20arch.jpg)

调度模块 -- filter

- Constraints
- Affinity
- Dependency
- Health filter
- Ports filter

调度策略 -- Strategy

- Binpack
- Spread
- Random

服务发现

- Ingress<外>

- Ingress + link(一个服务依赖于另一个服务)<内>

- 自定义网络<内>

  ```shell
  # 创建自定义网络
  docker network create --driver=overlay --attachable mynet
  
  # 创建服务
  docker service create -p 80:80 --network=mynet --name nginx nginx:latest
  ```

服务编排

- 服务部署 & 服务发现
- 服务更新 `docker service update`
- 服务扩缩容 `docker service scale`

Swarm 特点

- 对外以 Docker API 接口呈现
- 轻量，节省资源
- 插件化的机制
- 对Docker 命令支持完善

## 集群环境搭建

环境

- 3台 Linux 服务器，每台服务器上安装 Docker

- ip地址分别对应

  `192.168.1.102(主)`, `192.168.1.103`,`192.168.1.104`

  ```shell
  # 在 1.2 这个节点上执行
  docker swarm init --advertise-addr 192.168.1.102
  
  # 分别在 1.103 和 1.104 上运行
  docker swarm join --token SWMTKN-1-0zbj4o6v3p2r1lmagkegcliv6fzeul655wi5uoqcx9ve0sme2h-dexm5djiadqea6x64qlwcyzxe 192.168.1.102:2377
  
  # 使用 docker node ls 查看当节点的状态
  docker node ls
  
  # 在 swarm 集群的 Manager 节点中执行命令
  docker node promote ID/HOSTNAME
  
  # 创建服务
  docker service create --name test alpine ping www.baidu.com
  
  # 查看服务
  docker service ls
  
  # 查看服务详细信息
  docker service inspect test
  
  # 查看服务日志
  docker logs -f test
  
  # nginx 服务创建
  docker service create --name nginx nginx
  
  # 暴露服务运行端口
  docker service update --publish-add 8080:80 --detach=false nginx
  
  # 扩容 nginx，扩容的时候，并不是每台机器上各有一个nginx，有可能一台机器上有一个nginx，一个有两台
  docker service scale nginx=3
  
  # 在使用浏览器访问的时候会有服务的负载均衡机制
  ```

- 创建自己的网络

  ```shell
  # 创建自己的网络
  docker network create -d overlay mark
  
  # 创建服务使用自己创建的网络
  docker service create --network mark --name nginx -p 8888:80 nginx
  
  # 查看某服务运行在集群中那个容器中，记得修改服务的 hostname
  docker service ps nginx
  
  # 进入可以使用 ping 命令的容器中，这个容器网络是 mark，可以使用进行如下操作，看是否可以 ping 通
  docker service create --name test alpine:3.5 ping www.baidu.com
  docker service update --network-add mark test
  docker service ps test # 查看服务在那台机器的 docker 中运行
  docker exec -it ID sh # 进入容器
  ping nginx # 通过名字访问容器
  
  # 创建 dnsrr 方式的负载均衡，创建一个 EndPoint 类型为 dnsrr 的服务(容器间通过名字访问，不可指定端口)
  # 如果不希望服务被外面端口访问，建议定义成 dnsrr 的EndPoint
  docker service create --name nginx-b --endpoint-mode dnsrr --detach=false nginx
  
  # 为服务 nginx-b 添加网络
  docker service update --network-add mark nginx-b
  ```

- docker stack (服务栈/组)

  给一堆service 分组，在组里定义相互依赖的关系, [service.yml](https://github.com/MarkShen1992/docker-kubernetes-practice/blob/docker-swarm-env-deploy/docs/service.yml)  

  运行服务
  
  ```shell
  docker stack deploy -c service.yml test
  ```
  
  查看运行服务
  
  ```shell
  docker stack services test
  docker stack ps test
  docker stack ls
    
  docker service ls
  ```
  
  终止服务
  
  ```shell
  docker stack rm test
  ```
