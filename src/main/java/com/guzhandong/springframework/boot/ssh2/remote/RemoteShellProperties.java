package com.guzhandong.springframework.boot.ssh2.remote;


public class RemoteShellProperties {

    public static final String PREFIX = "spring.remote.exec";

    private String ip;
    /** 用户名 */
    private String osUsername;
    /** 密码 */
    private String password;

    private String charset = "UTF-8";
//    private String charset = Charset.defaultCharset().toString();

    private int timeout = 1000 * 5 * 60;

    /**
     * default
     */
    private int port = 22;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOsUsername() {
        return osUsername;
    }

    public void setOsUsername(String osUsername) {
        this.osUsername = osUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
