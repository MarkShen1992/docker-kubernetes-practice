# 注意
运行 `dubbo-demo-provider` 和 `dubbo-demo-consumer` 的时候要配置环境变量

```
-Djava.net.preferIPv4Stack=true
```

或者在主方法上添加程序代码

```
System.setProperty("java.net.preferIPv4Stack", "true");
```