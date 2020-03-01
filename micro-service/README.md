# 开发原则

- 找一些对别人依赖少的模块先开发

Thrift 安装
这里选择Linux操作系统来安装Thrift

configure: error: *** A compiler with support for C++11 language features is required.

http://ftp.gnu.org/gnu/gcc/

## GCC 4.8, 4.9, 5.3及格版本安装(虚机mini06)

- 4.8

  ```
  curl -Lks http://www.hop5.in/yum/el6/hop5.repo > /etc/yum.repos.d/hop5.repo
  yum install gcc gcc-g++ -y
  gcc --version
  ```

- 4.9

  ```
  yum install centos-release-scl -y
  yum install devtoolset-3-toolchain -y
  scl enable devtoolset-3 bash
  gcc --version
  ```

- 5.3

  ```
  yum install centos-release-scl -y
  yum install devtoolset-4-toolchain -y
  scl enable devtoolset-4 bash
  gcc --version
  ```

# Thrift demo

```shell
vim demo.thrift
```

> demo.thrift

```thrift
namespace java com.mark.thrift.demo
namespace py thrift.demo

service DemoService {
    void sayHello(1:string name);
}
```

```shell
# Java
thrift --gen java demo.thrift
# Python
thrift --gen py demo.thrift
```

# 注意事项

> docker_mysql_startup.sh

```shell
#!/bin/bash 
# 把变化的东西挂在到本机
cur_dir=`pwd`
docker stop mysql
docker rm mysql
docker run --name mysql -v ${cur_dir}/conf:/etc/mysql/conf.d -v ${cur_dir}/data:/var/lib/mysql -p 3307:3306 -e MYSQL_ROOT_PASSWORD=aA111111 -d mysq
l:latest
```

> Docker 常用命令总结

```shell
[root@localhost ~]# docker

Usage:  docker [OPTIONS] COMMAND

A self-sufficient runtime for containers

Options:
      --config string      Location of client config files (default "/root/.docker")
  -c, --context string     Name of the context to use to connect to the daemon (overrides 							 DOCKER_HOST env var and default context set with "docker 							   context use")
  -D, --debug              Enable debug mode
  -H, --host list          Daemon socket(s) to connect to
  -l, --log-level string   Set the logging level ("debug"|"info"|"warn"|"error"|"fatal") 							(default "info")
      --tls                Use TLS; implied by --tlsverify
      --tlscacert string   Trust certs signed only by this CA (default 		 
                		   "/root/.docker/ca.pem")
      --tlscert string     Path to TLS certificate file (default 
                           "/root/.docker/cert.pem")
      --tlskey string      Path to TLS key file (default "/root/.docker/key.pem")
      --tlsverify          Use TLS and verify the remote
  -v, --version            Print version information and quit

Management Commands:
  builder     Manage builds
  config      Manage Docker configs
  container   Manage containers
  context     Manage contexts
  engine      Manage the docker engine
  image       Manage images
  network     Manage networks
  node        Manage Swarm nodes
  plugin      Manage plugins
  secret      Manage Docker secrets
  service     Manage services
  stack       Manage Docker stacks
  swarm       Manage Swarm
  system      Manage Docker
  trust       Manage trust on Docker images
  volume      Manage volumes

Commands:
  attach      Attach local standard input, output, and error streams to a running container
  build       Build an image from a Dockerfile
  commit      Create a new image from a container's changes
  cp          Copy files/folders between a container and the local filesystem
  create      Create a new container
  diff        Inspect changes to files or directories on a container's filesystem
  events      Get real time events from the server
  exec        Run a command in a running container
  export      Export a container's filesystem as a tar archive
  history     Show the history of an image
  images      List images
  import      Import the contents from a tarball to create a filesystem image
  info        Display system-wide information
  inspect     Return low-level information on Docker objects
  kill        Kill one or more running containers
  load        Load an image from a tar archive or STDIN
  login       Log in to a Docker registry
  logout      Log out from a Docker registry
  logs        Fetch the logs of a container
  pause       Pause all processes within one or more containers
  port        List port mappings or a specific mapping for the container
  ps          List containers
  pull        Pull an image or a repository from a registry
  push        Push an image or a repository to a registry
  rename      Rename a container
  restart     Restart one or more containers
  rm          Remove one or more containers
  rmi         Remove one or more images
  run         Run a command in a new container
  save        Save one or more images to a tar archive (streamed to STDOUT by default)
  search      Search the Docker Hub for images
  start       Start one or more stopped containers
  stats       Display a live stream of container(s) resource usage statistics
  stop        Stop one or more running containers
  tag         Create a tag TARGET_IMAGE that refers to SOURCE_IMAGE
  top         Display the running processes of a container
  unpause     Unpause all processes within one or more containers
  update      Update configuration of one or more containers
  version     Show the Docker version information
  wait        Block until one or more containers stop, then print their exit codes

Run 'docker COMMAND --help' for more information on a command.

# 把变化的东西挂在到本机
```

# 开发过程

微服务开发场景，即业务场景，分析业务场景 => 划分业务，划分微服务，每个服务都实现什么样的功能，服务之间的关系，谁依赖谁，如何通讯。



## 业务场景

用户可以注册和登录

登录用户可以对课程进行CRUD操作

# 用户服务

- 用户登录
- 用户注册
- 用户基本信息查询
- 无状态，无session
- 单点登录

# 课程服务

- 登录验证
- 课程的CRUD

# 信息服务

- 发邮件
- 发短信

# 用户的 EdgeService

# 课程的 EdgeService

# API GATEWAY

# 服务调用关系