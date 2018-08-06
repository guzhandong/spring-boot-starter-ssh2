package com.guzhandong.springframework.boot.ssh2.poll;

import ch.ethz.ssh2.Connection;
import com.guzhandong.springframework.boot.ssh2.remote.RemoteShellProperties;
import com.guzhandong.springframework.boot.ssh2.utils.LogUtil;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;

public class RemoteExecConnectionFactory implements PooledObjectFactory<Connection>{

    private LogUtil logUtil = LogUtil.getLogger(getClass());

    private RemoteShellProperties remoteShellProperties;

    public RemoteExecConnectionFactory(RemoteShellProperties remoteShellProperties) {
        this.remoteShellProperties = remoteShellProperties;
    }

    private boolean login(Connection conn) throws IOException {
        logUtil.debug("remote login begin");
        long st = System.currentTimeMillis();
        boolean loginResult = conn.authenticateWithPassword(remoteShellProperties.getOsUsername(),remoteShellProperties.getPassword());
        long et = System.currentTimeMillis();
        logUtil.info("remote login complate, time:{}",et-st);
        return loginResult;
    }
    private void closeConnection(Connection conn) throws IOException {
        logUtil.debug("remote connection close begin");
        long st = System.currentTimeMillis();
        conn.close();
        long et = System.currentTimeMillis();
        logUtil.info("remote connection close complate, time:{}",et-st);
    }

    private Connection createConnection() throws IOException {
        Connection conn = new Connection(remoteShellProperties.getIp());
        conn.connect();
        logUtil.debug("craete connection success");
        return conn;
    }

    @Override
    public PooledObject<Connection> makeObject() throws Exception {
        PooledObject<Connection> pooledObject = new DefaultPooledObject(createConnection());
        return pooledObject;
    }

    @Override
    public void destroyObject(PooledObject<Connection> pooledObject) throws Exception {
        synchronized (pooledObject.getObject()) {
            closeConnection(pooledObject.getObject());
        }
    }

    @Override
    public boolean validateObject(PooledObject<Connection> pooledObject) {
        return pooledObject.getObject()!=null;
    }

    @Override
    public void activateObject(PooledObject<Connection> pooledObject) throws Exception {
        synchronized (pooledObject.getObject()) {
            if (!pooledObject.getObject().isAuthenticationComplete()) {
                boolean loginResult = login(pooledObject.getObject());
            }
        }
    }

    @Override
    public void passivateObject(PooledObject<Connection> pooledObject) throws Exception {

    }
}
