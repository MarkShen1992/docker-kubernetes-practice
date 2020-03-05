# 原则

```
有跟用户不相关的业务一律放到业务端去维护数据的关系，相对于 course 系统来说， 用户系统为一个公共系统
```

# build.sh 不能运行

```
-bash: ./build.sh: /bin/bash^M: 坏的解释器: 没有那个文件或目录
vim build.sh
:set ff=unix
:wq
```

# docker 运行

```
docker run -it course-edge-service:latest --zk.address=192.168.1.102
```