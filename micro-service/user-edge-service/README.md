微服务的依赖可以使用 link 的方式，根据名字搜索到依赖

大前提: 所有不在这开发中的模块，是外围的服务，会有一个 IP，会有一个相对固定的服务接入方式

微服务和微服务之间的通讯，微服务和外围服务的通讯

# build.sh 不能运行

```
-bash: ./build.sh: /bin/bash^M: 坏的解释器: 没有那个文件或目录
vim build.sh
:set ff=unix
:wq
```

# docker 运行

```
docker run -it user-edge-service:latest --redis.address=127.0.0.1
```