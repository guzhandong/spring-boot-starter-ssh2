package com.guzhandong.springframework.boot.ssh2.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class RemoteExecPoolConfigure extends GenericObjectPoolConfig {
    public static final String PREFIX = "spring.remote.exec.pool";
}
