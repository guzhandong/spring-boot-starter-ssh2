package com.guzhandong.springframework.boot.ssh2.remote;


import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.guzhandong.springframework.boot.ssh2.exception.impl.RemoteExecException;
import com.guzhandong.springframework.boot.ssh2.utils.LogUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * 原则调用shell，执行器，暂时不保持connection 和session ，不做池。
 * 每个执行操作都会创建连接，打开session，执行完shell 后关闭session，关闭 connection
 */
public class RemoteShellExecutor {

    private LogUtil logUtil = LogUtil.getLogger(getClass());

    private Connection conn;
    /** 远程机器IP */
    private String ip;
    /** 用户名 */
    private String osUsername;
    /** 密码 */
    private String password;

    //default
//    private String charset = Charset.defaultCharset().toString();
    private String charset = "UTF-8";

    //default
    private int timeOut = 1000 * 5 * 60;

    /**
     *
     * @param ip
     * @param osUsername
     * @param password
     * @param charset
     */
    public RemoteShellExecutor(String ip, String osUsername, String password, String charset) {
        this.ip = ip;
        this.osUsername = osUsername;
        this.password = password;
        this.charset = charset;
    }

    /**
     *
     * @param ip
     * @param osUsername
     * @param password
     * @param charset
     * @param timeOut
     */
    public RemoteShellExecutor(String ip, String osUsername, String password, String charset, int timeOut) {
        this.ip = ip;
        this.osUsername = osUsername;
        this.password = password;
        this.charset = charset;
        this.timeOut = timeOut;
    }

    private boolean login() throws IOException {
        logUtil.debug("remote login begin");
        long st = System.currentTimeMillis();
        conn = new Connection(ip);
        conn.connect();
        boolean loginResult = conn.authenticateWithPassword(this.osUsername,this.password);
        long et = System.currentTimeMillis();
        logUtil.info("remote login complate, time:{}",et-st);
        return loginResult;
    }

    private void logOut() {
        try {
            logUtil.debug("remote logout begin");
            long st = System.currentTimeMillis();
            conn.close();
            long et = System.currentTimeMillis();
            logUtil.info("remote logout complate, time:{}",et-st);
        } catch (Exception e) {
            logUtil.debug("logout, connection close error :",e);
        }
    }

    public int exec(String cmd) {
        InputStream stuOut = null;
        InputStream stuErr = null;
        String outStr = "";
        String outErr = "";
        int result = -1;

        try {
            if (login()) {
                logUtil.info("remote exec begin,cmd : {}",cmd);
                long st = System.currentTimeMillis();
                Session session = conn.openSession();
                session.execCommand(cmd);
                stuOut = session.getStdout();
                stuErr = session.getStderr();
                outStr = stream2String(stuOut,this.charset);
                outErr = stream2String(stuErr,this.charset);
                session.waitForCondition(ChannelCondition.EXIT_STATUS, timeOut);
                logUtil.debug("stuout : {}",outStr);
                logUtil.debug("stuerr : {}",outErr);
                long et = System.currentTimeMillis();
                logUtil.info("remote exec complate,time :{} ,cmd : {}",et-st,cmd);
                result = session.getExitStatus();
                session.close();
            }
        } catch (Exception e) {
            //login exception
            logUtil.error("remote auth error,msg :{}",e.getMessage());
            throw new RemoteExecException("远程调用脚本异常",e);
        } finally {
            logOut();
        }
        return  result;
    }

    private String stream2String(InputStream inputStream,String charset) throws Exception{
        byte [] buff = new byte[1024];
        StringBuilder stringBuilder = new StringBuilder();
        while (inputStream.read(buff)!=-1) {
            stringBuilder.append(new String(buff,charset));
        }
        return stringBuilder.toString();
    }
}
