## k8s安装

- 由于本环境 k8s 版本是 k8s1.9， 其 [对应的 docker 版本][1] 是

  - **`1.11.2`** to **`1.13.1`**
  - **`17.03.x`**
  
- 可以安装 `Docker17.03.x` 的 `CentOS` 版本

  - [`CentOS-7-x86_64-Minimal-2003.iso`][2]

- 环境准备

  | 系统类型 | ip地址        | 节点角色 | CPU  | Memory | Hostname |
  | -------- | ------------- | -------- | ---- | ------ | -------- |
  | CentOS 7 | 192.168.31.50 | Master   | 1    | 2G     | server01 |
  | CentOS 7 | 192.168.31.51 | Worker   | 1    | 2G     | server02 |
  | CentOS 7 | 192.168.31.52 | Worker   | 1    | 2G     | server03 |

- 安装 `docker`，[软件包下载地址][3]

  ```shell
  docker-engine-selinux-17.03.1.ce-1.el7.centos.noarch.rpm
  docker-engine-17.03.1.ce-1.el7.centos.x86_64.rpm
  docker-engine-debuginfo-17.03.1.ce-1.el7.centos.x86_64.rpm
  
  # 下载好这三个软件包后，执行安装命令
  yum localinstall -y *.rpm
  systemctl enable docker
  ```

- 配置docker镜像加速

  ```shell
  cat <<EOF > /etc/docker/daemon.json
  {
      "registry-mirrors": [
       	"https://3laho3y3.mirror.aliyuncs.com",
       	"http://hub-mirror.c.163.com",
       	"http://f1361db2.m.daocloud.io",
       	"https://docker.mirrors.ustc.edu.cn",
       	"https://registry.docker-cn.com",
       	"https://mirror.ccs.tencentyun.com"
    	]
  }
  EOF
  ```

- 在文件`/usr/lib/systemd/system/docker.service` 添加下面的代码， 接受所有ip的数据包转发

  ```
  #找到ExecStart=xxx，在这行上面加入一行，内容如下：(k8s的网络需要)
  ExecStartPost=/sbin/iptables -I FORWARD -s 0.0.0.0/0 -j ACCEPT
  ```

- 重启docker服务

  ```shell
  systemctl daemon-reload
  service docker restart
  ```

- 系统设置

  - 关闭所有节点防火墙

    ```shell
    systemctl stop firewalld.service
    systemctl status firewalld.service
    
    man systemctl
    ```

  - 设置系统参数 - 允许路由转发，不对bridge的数据进行处理

    ```shell
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

    ```shell
    #配置host，使每个Node都可以通过名字解析到ip地址
    $ vi /etc/hosts
    #加入如下片段(ip地址和servername替换成自己的)
    192.168.31.50 server01
    192.168.31.51 server02
    192.168.31.52 server03
    ```

  - 上传 `k8s` 二进制安装包文件到虚拟电脑中

    ```
    链接: https://pan.baidu.com/s/1dzOM2rRuNxJ3-0dbMYY-4w 提取码: k2va 
    ```

  - 解压文件到目录 `/usr/local` 下

  - 在文件 `/etc/profile` 中添加 path

  - 重新生效 `source /etc/profile`

  > **注意：**
  >
  > - 本文中所有脚本取于 [kubernetes-starter](https://github.com/liuyi01/kubernetes-starter)
  > - 一定要**关闭电脑防火墙**
  > - 去除 `^M` 字符
  >   - `dos2unix filename`
  >   - `sed -e ‘s/^M/\n/g’ filename`
  >   - 使用 vi 或 vim， set ff=unix


- 安装 `etcd` 在**主节点**上

  ```shell
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

- 安装 `apiserver` 在**主节点**上

  ```shell
  cp ~/kubernetes-starter/target/master-node/kube-apiserver.service /usr/lib/systemd/system/
  systemctl enable kube-apiserver.service
  service kube-apiserver start
  journalctl -f -u kube-apiserver
  ```

- 安装 `Controller Manager` 在**主节点**上

  ```shell
  cp ~/kubernetes-starter/target/master-node/kube-controller-manager.service /usr/lib/systemd/system/
  systemctl enable kube-controller-manager.service
  service kube-controller-manager start
  journalctl -f -u kube-controller-manager
  ```

- 安装 `Scheduler` 在**主节点**上

  ```shell
  cp ~/kubernetes-starter/target/master-node/kube-scheduler.service /usr/lib/systemd/system/
  systemctl enable kube-scheduler.service
  service kube-scheduler start
  journalctl -f -u kube-scheduler
  ```

- 部署 `CalicoNode` 在**所有节点**上

  ```shell
  cp ~/kubernetes-starter/target/all-node/kube-calico.service /usr/lib/systemd/system/
  systemctl enable kube-calico.service
  service kube-calico start
  journalctl -f -u kube-calico
  ```

- 配置 `kubectl` 命令（任意节点）

  ```shell
  # 设置 api-server 和 上下文
  # #指定apiserver地址（ip替换为你自己的api-server地址）
  kubectl config set-cluster kubernetes  --server=http://192.168.31.50:8080
  
  # #指定设置上下文，指定cluster
  kubectl config set-context kubernetes --cluster=kubernetes
  
  #选择默认的上下文
  kubectl config use-context kubernetes
  ```

- 配置 `kubelet` (Worker节点)

  ```shell
  #确保相关目录存在
  mkdir -p /var/lib/kubelet
  mkdir -p /etc/kubernetes
  mkdir -p /etc/cni/net.d
  
  #复制kubelet服务配置文件
  cp ~/kubernetes-starter/target/worker-node/kubelet.service /usr/lib/systemd/system/
  cp ~/kubernetes-starter/target/worker-node/kubelet.kubeconfig /etc/kubernetes/
  cp ~/kubernetes-starter/target/worker-node/10-calico.conf /etc/cni/net.d/
  
  systemctl enable kubelet.service
  service kubelet start
  journalctl -f -u kubelet
  ```

  至此，kubernetes集群就可以工作了。

- kubernetes 入门命令

  ```shell
  # 在 master 节点上执行
  kubectl version
  
  # 获取所有节点信息
  kubectl get nodes
  
  # 获取 pods
  kubectl get pods
  
  # help
  kubectl --help
  
  # deployment
  kubectl run kubernetes-bootcamp --image=jocatalin/kubernetes-bootcamp:v1 --port=8080
  
  # 查看所有 deployments 
  kubectl get deployments
  
  # 查看 pod 的信息
  kubectl get pods
  
  # 查看更多的信息
  kubectl get pods -o wide
  会多出两个字字段，pod的IP，node的IP
  
  # 删除部署
  kubectl delete deployments kubernetes-bootcamp
  
  # 删除 pod
  kubectl delete pods nginx
  
  # kubectl get --help 查看短名字
  kubectl get deploy
  
  # kubectl describe 部署
  kubectl describe deploy kubernetes-bootcamp
  
  # 查看 pod
  kubectl get pods
  kubectl describe pods
  kubectl describe pods <pods-name>
  
  [root@server01 docs]# kubectl get pods
  NAME                                   READY     STATUS    RESTARTS   AGE
  kubernetes-bootcamp-6b7849c495-7jj8f   1/1       Running   0          18m
  如何访问名字为 kubernetes-bootcamp-6b7849c495-7jj8f 的服务呢？
  使用 kube-proxy
  
  # 启动 kube-proxy
  kubectl proxy
  
  # 另起一个窗口
  [root@server01 ~]# curl localhost:8001/api/v1/proxy/namespaces/default/pods/kubernetes-bootcamp-6b7849c495-7jj8f/
  Hello Kubernetes bootcamp! | Running on: kubernetes-bootcamp-6b7849c495-7jj8f | v=1
  
  # 扩缩容
  kubectl scale deploy kubernetes-bootcamp --replicas=4
  
  # 查看下效果
  kubectl get deploy
  
  # 缩回到2个
  kubectl scale deploy kubernetes-bootcamp --replicas=2
  
  # 更新镜像
  kubectl set image deploy kubernetes-bootcamp kubernetes-bootcamp=jocatalin/kubernetes-bootcamp:v2
  
  # 查看状态
  kubectl rollout status deploy kubernetes-bootcamp
  
  # 查看是否更新成功
  kubectl describe deploy kubernetes-bootcamp
  
  # 回滚
  kubectl rollout undo deploy kubernetes-bootcamp
  kubectl rollout status deploy kubernetes-bootcamp
  ```

- 除了通过命令行管理 k8s , 还可以通过配置文件的方式去管理

  `nginx` Pod 管理 `nginx-pod.yaml`

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: nginx
  spec:
    containers:
      - name: nginx
        image: nginx:1.7.9
        ports:
        - containerPort: 80
  ```

  ```shell
  # 使用命令创建 pod
  kubectl create -f nginx-pod.yaml
  
  # 查看 pod 是否起来了
  kubectl get pods
  
  # 注意区分 pod 和 deploy 概念
  # kubectl run 的时候是创建了一个 deploy
  # 使用代理 kubectl proxy 访问 nginx
  curl localhost:8001/api/v1/proxy/namespaces/default/pods/nginx/
  ```

  创建`nginx-deployment.yaml` 

  ```yaml
  apiVersion: apps/v1beta1
  kind: Deployment
  metadata:
    name: nginx-deployment
  spec:
    replicas: 2
    template:
      metadata:
        labels:
          app: nginx
      spec:
        containers:
        - name: nginx
          image: nginx:1.7.9
          ports:
          - containerPort: 80
  ```

  ```shell
  # 部署 deploy
  kubectl create -f nginx-deployment.yaml
  kubectl get pods -l app=nginx
  ```

- 为集群添加 service 功能 - kube-proxy (工作节点)

  ```shell
  # 确保工作目录存在
  mkdir -p /var/lib/kube-proxy
  
  # 复制kube-proxy服务配置文件
  cp ~/kubernetes-starter/target/worker-node/kube-proxy.service /usr/lib/systemd/system/
  
  # 复制kube-proxy依赖的配置文件
  cp ~/kubernetes-starter/target/worker-node/kube-proxy.kubeconfig /etc/kubernetes/
  
  systemctl enable kube-proxy.service
  service kube-proxy start
  journalctl -f -u kube-proxy
  
  # k8s service : api-server 创建的时候就有了这个服务
  kubectl get services
  
  # 查看服务信息
  kubectl describe services kubernetes
  
  # 使用kube-proxy访问到我们的pod
  # target-port：容器的端口
  # port：服务的端口
  kubectl expose deploy nginx-deployment --type="NodePort" --target-port=80 --port=80
  
  # 查看 service 的个数
  kubectl get services
  ```

- `nginx-service.yaml`

  ```
  apiVersion: v1
  kind: Service
  metadata:
    name: nginx-service
  spec:
    ports:
    - port: 8080
      targetPort: 80
      nodePort: 20000
    selector:
      app: nginx
    type: NodePort
  ```

  ```yaml
  # 创建 kube-service
  kubectl create -f nginx-service.yaml
  kubectl get svc
  ```

- 安装 `kube-dns` 组件

  ```shell
  kubectl create -f kube-dns.yaml
  kubectl -n kube-system get svc # kube-system 为内部的命名空间
  kubectl -n kube-system get deploy
  kubectl -n kube-system get pods
  ```

- 添加认证授权功能

  ......



[1]:https://stackoverflow.com/questions/48950827/docker-version-supported-in-kubernetes-1-9
[2]:http://mirrors.sohu.com/centos/7/isos/x86_64/
[3]:https://mirrors.mediatemple.net/docker/centos/7/Packages/

