# 部署 Mesos 遇到的问题

> **在做实验的时候关闭服务器的防火墙**

```shell
systemctl stop firewalld
```

```
WARNING: Logging before InitGoogleLogging() is written to STDERR
I0311 02:16:26.773185     1 main.cpp:348] Build: 2017-11-18 03:00:43 by ubuntu
I0311 02:16:26.773356     1 main.cpp:349] Version: 1.4.1
I0311 02:16:26.773360     1 main.cpp:352] Git tag: 1.4.1
I0311 02:16:26.773363     1 main.cpp:356] Git SHA: c844db9ac7c0cef59be87438c6781bfb71adcc42
I0311 02:16:26.780624     1 logging.cpp:194] INFO level logging started!
I0311 02:16:26.786715     1 systemd.cpp:238] systemd version `229` detected
I0311 02:16:26.786765     1 main.cpp:459] Initializing systemd state
E0311 02:16:26.786842     1 main.cpp:468] EXIT with status 1: Failed to initialize systemd: Failed to locate systemd runtime directory: /run/systemd/system
```

> 查看方法 docker run -it mesosphere/mesos-slave:1.4.1 --help | grep system

Marathon Task Shell

```shell
while true; do sleep 1; echo 'hello marathon'; done
```

