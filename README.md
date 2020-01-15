### dahuaboke-rpc

纯自己手动编写的rpc框架，已经做成了starter，使用超级方便，天然支持高可用。

### 性能测试

单节点调用（传输字符串类型参数）响应时间不超过100毫秒（本机测试，注册中心在服务器，服务器带宽为1M）。

单节点调用500QPS正常响应。

### 集群调度方式

现在支持随机调度提供者方式

### 使用须知

下载源码，打包引入

```
<dependency>
    <groupId>com.dahuaboke</groupId>
    <artifactId>rpc</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

**只需要在service层将@Service注解替换成@RpcService，调用方无感知，不需要修改任何代码。**

### 端口

消费者无端口占用

提供者默认采用13579端口，当同一台机器部署多个提供者时，会自动向下寻找端口

### 使用配置

##### provider（提供者）

```
#使用者身份（提供者）
rpc.role=provider
#消费者暴漏的本机ip（容器化部署时可以通过端口映射之后填写宿主机ip）
rpc.localIp=localhost
#注册中心地址
rpc.regist.address=localhost:2181 
```

##### consumer（消费者）

```
#使用者身份（消费者）
rpc.role=consumer
#注册中心地址
rpc.regist.address=zookeeper://localhost:2181
```

### 扩展

扩展接口已经预留，注册中心扩展方法预留，实时动态切换提供者方法已经预留。

### 联系我

**加我请注明来意**

邮箱：
    love_dingwq@163.com
    970263611@qq.com

微信：
    dingweiqiang872226

QQ  ：
    970263611

