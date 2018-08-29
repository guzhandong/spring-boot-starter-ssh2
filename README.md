# spring-boot-starter-ssh2

远程执行shell的springboot依赖包,其中使用commons-pool2实现了简单的连接池。

> author : guzhandong  

> email : 569199386@qq.com

> springboot version : 2.0.2.RELEASE



## quickstart

> install project for maven
```
git pull git@github.com:guzhandong/spring-boot-starter-ssh2.git
cd spring-boot-starter-ssh2 
mvn clean install
```


> add dependent property in pom.xml

```
<dependency>
    <groupId>com.guzhandong.springframework.boot</groupId>
    <artifactId>spring-boot-starter-ssh2</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

```


> add property in application.yml


```
spring:
  remote:
    exec:
      ip: 192.168.200.89
      port: 22
      osUsername: username
      password: password

```




> create java file



```


import com.knowlegene.springframework.boot.ssh2.remote.RemoteShellExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestRemote {

    @Autowired
    private RemoteShellExecutor remoteShellExecutor;

    public void lsDir() {
        int execResult = remoteShellExecutor.exec("ls /home/ops");
    }

}

```



## all configuration
```
spring:
  remote:
    exec:
      ip: 192.168.200.89
      port: 22
      osUsername: username
      password: password
      charset: utf-8
      timeout: 300000
      pool:
        

```

## todo list

1. 修复JMX在springboot中和eureka集成导致注册失败，当前默认设置为关闭连接池的JMX来保证集成可用。