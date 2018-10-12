package com.personal.springboot.scheduled.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.personal.springboot.common.utils.FtpUtils;


@Configuration
@ConfigurationProperties(prefix = "ftp.conf")
public class FtpConfUtilsConf {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpConfUtilsConf.class);

    private String server;
    private int port;
    private String uname;
    private String password;

    @Bean
    public FtpUtils getFtpUtils() {
        LOGGER.info("获取FtpUtils执行开始,server={},port={},uname={},password={}",server,port,uname,password);
        FtpUtils ftpUtils = new FtpUtils(server, port, uname, password);
        LOGGER.info("FtpUtils初始化完成...");
        return ftpUtils;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}