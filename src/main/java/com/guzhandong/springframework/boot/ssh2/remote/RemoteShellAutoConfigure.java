package com.guzhandong.springframework.boot.ssh2.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({RemoteShellExecutor.class, RemoteShellProperties.class})
public class RemoteShellAutoConfigure {

    @Bean
    @ConfigurationProperties(prefix = RemoteShellProperties.PREFIX)
    @ConditionalOnProperty(prefix = RemoteShellProperties.PREFIX,value = {"ip","osUsername"})
    @ConditionalOnMissingBean(RemoteShellProperties.class)
    public RemoteShellProperties remoteShellProperties(){
        return new RemoteShellProperties();
    }

    @Bean
    @ConditionalOnBean(RemoteShellProperties.class)
    @ConditionalOnSingleCandidate(RemoteShellProperties.class)
    @ConditionalOnMissingBean(RemoteShellExecutor.class)
    public RemoteShellExecutor remoteShellExecutor(@Autowired RemoteShellProperties remoteShellProperties) {
        return new RemoteShellExecutor(remoteShellProperties.getIp(),remoteShellProperties.getOsUsername(),remoteShellProperties.getPassword(),remoteShellProperties.getCharset(),remoteShellProperties.getTimeout());
    }
}
