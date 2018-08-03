# spring-boot-starter-ssh2

远程执行shell的springboot依赖包

> author : guzhandong  

> email : 569199386@qq.com

> springboot versioni : 2.0.2.RELEASE



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
      osUsername: ops
      password: ops@2018

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
      osUsername: ops
      password: ops@2018
      charset: utf-8
      timeout: 300000

```

## todo list

1. connection poll