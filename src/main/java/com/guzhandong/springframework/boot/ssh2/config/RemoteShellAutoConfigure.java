package com.guzhandong.springframework.boot.ssh2.config;

import com.guzhandong.springframework.boot.ssh2.pool.RemoteExecConnectionFactory;
import com.guzhandong.springframework.boot.ssh2.pool.RemoteExecConnectionPool;
import com.guzhandong.springframework.boot.ssh2.pool.RemoteExecPoolConfigure;
import com.guzhandong.springframework.boot.ssh2.remote.RemoteShellExecutor;
import com.guzhandong.springframework.boot.ssh2.remote.RemoteShellProperties;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({RemoteShellExecutor.class, RemoteShellProperties.class, GenericObjectPool.class})
public class RemoteShellAutoConfigure {

    @Bean
    @ConfigurationProperties(prefix = RemoteShellProperties.PREFIX)
    @ConditionalOnProperty(prefix = RemoteShellProperties.PREFIX,value = {"ip","osUsername"})
    @ConditionalOnMissingBean(RemoteShellProperties.class)
    public RemoteShellProperties remoteShellProperties(){
        return new RemoteShellProperties();
    }


    @Bean
    @ConfigurationProperties(prefix = RemoteExecPoolConfigure.PREFIX)
    @ConditionalOnMissingBean(RemoteExecPoolConfigure.class)
    public RemoteExecPoolConfigure remoteExecPoolConfigure() {
        RemoteExecPoolConfigure remoteExecPoolConfigure = new RemoteExecPoolConfigure();
        //TODO  开启jmx 导致  springboot（version:2.0.1，内嵌tomcat，和euraka集成），启动后(erueka 注册日志:registration status: 404)立即关闭servlet 容器，暂时默认设置jmx为关闭状态，再寻找原因
        remoteExecPoolConfigure.setJmxEnabled(false);
        return remoteExecPoolConfigure;
    }

    @Bean
    @ConditionalOnBean(RemoteShellProperties.class)
    @ConditionalOnSingleCandidate(RemoteShellProperties.class)
    @ConditionalOnMissingBean(RemoteExecConnectionFactory.class)
    public RemoteExecConnectionFactory remoteExecConnectionFactory(
            @Autowired RemoteShellProperties remoteShellProperties) {
        return new RemoteExecConnectionFactory(remoteShellProperties);
    }

    @Bean
    @ConditionalOnBean({RemoteExecConnectionFactory.class,RemoteExecPoolConfigure.class})
    @ConditionalOnMissingBean(RemoteExecConnectionPool.class)
    public RemoteExecConnectionPool remoteExecConnectionPool(
            @Autowired RemoteExecConnectionFactory remoteExecConnectionFactory,
            @Autowired RemoteExecPoolConfigure remoteExecPoolConfigure) {
        return new RemoteExecConnectionPool(remoteExecConnectionFactory,remoteExecPoolConfigure);
    }

    @Bean
    @ConditionalOnBean({RemoteExecConnectionPool.class,RemoteShellProperties.class})
    @ConditionalOnMissingBean(RemoteShellExecutor.class)
    public RemoteShellExecutor remoteShellExecutor(
            @Autowired RemoteShellProperties remoteShellProperties,
            @Autowired RemoteExecConnectionPool remoteExecConnectionPool) {
        return new RemoteShellExecutor(remoteExecConnectionPool,remoteShellProperties.getCharset(),remoteShellProperties.getTimeout());
    }
}
