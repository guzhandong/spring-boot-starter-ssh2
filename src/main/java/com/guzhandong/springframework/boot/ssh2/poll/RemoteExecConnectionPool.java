package com.guzhandong.springframework.boot.ssh2.poll;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class RemoteExecConnectionPool extends GenericObjectPool {

    public RemoteExecConnectionPool(PooledObjectFactory factory) {
        super(factory);
    }

    public RemoteExecConnectionPool(PooledObjectFactory factory, GenericObjectPoolConfig config) {
        super(factory, config);
    }

    public RemoteExecConnectionPool(PooledObjectFactory factory, GenericObjectPoolConfig config, AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }
}
