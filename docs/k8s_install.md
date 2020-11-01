## 6k8s安装

- 环境准备

  | 系统类型 | ip地址        | 节点角色 | CPU  | Memory | Hostname |
  | -------- | ------------- | -------- | ---- | ------ | -------- |
  | CentOS 7 | 192.168.31.46 | Master   | 1    | 2G     | server01 |
  | CentOS 7 | 192.168.31.47 | Worker   | 1    | 2G     | server02 |
  | CentOS 7 | 192.168.31.48 | Worker   | 1    | 2G     | server03 |

- 安装 docker, 可以参照[官方文档](https://docs.docker.com/engine/install/centos/)

- 在文件`/usr/lib/systemd/system/docker.service` 添加下面的代码， 接受所有ip的数据包转发

  ```
  #找到ExecStart=xxx，在这行上面加入一行，内容如下：(k8s的网络需要)
  ExecStartPost=/sbin/iptables -I FORWARD -s 0.0.0.0/0 -j ACCEPT
  ```

- 重启docker服务

  ```
  systemctl daemon-reload
  service docker restart
  ```

- 系统设置

  - 关闭所有节点防火墙

    ```
    systemctl stop firewalld.service
    systemctl status firewalld.service
    
    man systemctl
    ```

  - 设置系统参数 - 允许路由转发，不对bridge的数据进行处理

    ```
    #写入配置文件
    $ cat <<EOF > /etc/sysctl.d/k8s.conf
    net.ipv4.ip_forward = 1
    net.bridge.bridge-nf-call-ip6tables = 1
    net.bridge.bridge-nf-call-iptables = 1
    EOF
     
    #生效配置文件
    $ sysctl -p /etc/sysctl.d/k8s.conf
    ```

  - 配置`hosts`文件

    ```
    #配置host，使每个Node都可以通过名字解析到ip地址
    $ vi /etc/hosts
    #加入如下片段(ip地址和servername替换成自己的)
    192.168.31.46 server01
    192.168.31.47 server02
    192.168.31.48 server03
    ```

  - 上传 `k8s` 二进制安装包文件到虚拟电脑中

    ```
    链接: https://pan.baidu.com/s/1dzOM2rRuNxJ3-0dbMYY-4w 提取码: k2va 
    ```

  - 解压文件到目录 `/usr/local` 下

  - 在文件 `/etc/profile` 中添加 path

- 安装 `etcd` 在主节点上

  ```
  #把服务配置文件copy到系统服务目录
  cp ~/kubernetes-starter/target/master-node/etcd.service /usr/lib/systemd/system/
  #enable服务
  systemctl enable etcd.service
  #创建工作目录(保存数据的地方)
  mkdir -p /var/lib/etcd
  # 启动服务
  service etcd start
  # 查看服务日志，看是否有错误信息，确保服务正常
  journalctl -f -u etcd.service
  ```

- 安装 `apiserver` 在主节点上

  ```
  cp ~/kubernetes-starter/target/master-node/kube-apiserver.service /usr/lib/systemd/system/
  systemctl enable kube-apiserver.service
  service kube-apiserver start
  journalctl -f -u kube-apiserver
  ```

- 安装 `Controller Manager` 在主节点上

  ```
  cp ~/kubernetes-starter/target/master-node/kube-controller-manager.service /usr/lib/systemd/system/
  systemctl enable kube-controller-manager.service
  service kube-controller-manager start
  journalctl -f -u kube-controller-manager
  ```

- 安装 `Scheduler` 在主节点上

  ```
  cp ~/kubernetes-starter/target/master-node/kube-scheduler.service /usr/lib/systemd/system/
  systemctl enable kube-scheduler.service
  service kube-scheduler start
  journalctl -f -u kube-scheduler
  ```

- 部署 `CalicoNode` 在所有节点

  ```
  cp ~/kubernetes-starter/target/all-node/kube-calico.service /usr/lib/systemd/system/
  systemctl enable kube-calico.service
  service kube-calico start
  journalctl -f -u kube-calico
  ```

  