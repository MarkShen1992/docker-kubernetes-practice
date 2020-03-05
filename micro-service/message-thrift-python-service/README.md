# Docker 运行容器命令
```
docker run -it message-service:latest
```

# build.sh 不能运行

```
-bash: ./build.sh: /bin/bash^M: 坏的解释器: 没有那个文件或目录
vim build.sh
:set ff=unix
:wq
```