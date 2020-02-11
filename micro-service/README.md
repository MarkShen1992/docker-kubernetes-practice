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

开发原则
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

