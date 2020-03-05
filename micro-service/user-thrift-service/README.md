# Docker 运行容器命令
```
docker run -it user-service:latest --mysql.address=192.168.1.101 --username=root --password=aA111111
```

# build.sh 不能运行

```
-bash: ./build.sh: /bin/bash^M: 坏的解释器: 没有那个文件或目录
vim build.sh
:set ff=unix
:wq
```