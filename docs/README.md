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

