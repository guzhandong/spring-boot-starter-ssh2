package com.guzhandong.springframework.boot.ssh2.poll;

import ch.ethz.ssh2.Connection;
import com.guzhandong.springframework.boot.ssh2.remote.RemoteShellProperties;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
* RemoteExecConnectionFactory Tester. 
* 
* @author <Authors name> 
* @since <pre>八月 5, 2018</pre> 
* @version 1.0 
*/ 
public class RemoteExecConnectionFactoryTest {

    public static void main(String[] args) throws Exception {
        RemoteShellProperties remoteShellProperties = new RemoteShellProperties();
        remoteShellProperties.setIp("192.168.200.89");
        remoteShellProperties.setOsUsername("ops");
        remoteShellProperties.setPassword("know@2018");
        RemoteExecPoolConfigure remoteExecPoolConfigure = new RemoteExecPoolConfigure();
        remoteExecPoolConfigure.setMinIdle(3);
        remoteExecPoolConfigure.setMaxTotal(4);
        remoteExecPoolConfigure.setTestOnBorrow(true);
        remoteExecPoolConfigure.setTestOnCreate(true);
        remoteExecPoolConfigure.setTestOnReturn(true);
//        remoteExecPoolConfigure.setBlockWhenExhausted(true);
        remoteExecPoolConfigure.setMinEvictableIdleTimeMillis(1000);
        remoteExecPoolConfigure.setTimeBetweenEvictionRunsMillis(5000);
        ObjectPool<Connection> op = new GenericObjectPool<Connection>(new RemoteExecConnectionFactory(remoteShellProperties),remoteExecPoolConfigure);

        r(op);
        r(op);
        r(op);
        r(op);
        r(op);
        Thread.sleep(3000);
        op.close();
        System.out.println("main thread complate");

        //todo  阻塞获取连接池连接的时候 ，关闭池，貌似存在死锁。
        //todo  阻塞获取连接池连接的时候 ，sleeP主线程，在主线程关闭池，提示 InterruptedException。

    }

    public static void r(ObjectPool<Connection> op) {
        new Thread(()->{
            Connection connection = null;
            try {
                connection = op.borrowObject();
                System.out.println(Thread.currentThread().getName());
                Thread.sleep(5000);
                op.returnObject(connection);
                System.out.println(op.getNumIdle());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

} 
