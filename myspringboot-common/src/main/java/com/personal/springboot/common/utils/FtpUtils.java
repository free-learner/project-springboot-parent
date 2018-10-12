package com.personal.springboot.common.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FTP协议里面，规定文件名编码为iso-8859-1，所以目录名或文件名需要转码。
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年7月5日
 */
@SuppressWarnings("unused")
public class FtpUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FtpUtils.class);

    private FTPClient ftp = null;
    /**
     * Ftp服务器
     */
    private String server;
    /**
     * 用户名
     */
    private String uname;
    /**
     * 密码
     */
    private String password;
    /**
     * 连接端口，默认21
     */
    private int port = 21;

    public FtpUtils(String server, int port, String uname, String password) {
        this.server = server;
        if (port > 0) {
            this.port = port;
        }
        this.uname = uname;
        this.password = password;
        // 初始化
        ftp = new FTPClient();
    }

    /** 本地字符编码 */
    public static String LOCAL_CHARSET = "GBK";
//    private static String LOCAL_CHARSET = "UTF-8";
     
    // FTP协议里面，规定文件名编码为iso-8859-1
    public static String SERVER_CHARSET = "ISO-8859-1";
    
    static{
        LOCAL_CHARSET=System.getProperty("file.encoding");
        LOGGER.info("静态获取的file.encoding本地编码格式为:{}",LOCAL_CHARSET);
    }
    
    /**
     * 连接FTP服务器
     * 
     * @param server
     * @param uname
     * @param password
     * @return
     * @throws Exception
     */
    public FTPClient connectFTPServer() throws Exception {
        try {
            ftp.connect(this.server, this.port);
            //下面三行代码必须要，而且不能改变编码格式，否则不能正确下载中文文件
            ftp.configure(getFTPClientConfig());
            
            boolean loginFlag = ftp.login(this.uname, this.password);
            LOGGER.info("登录结果为:{}",loginFlag);
            if (!loginFlag) {
                ftp.logout();
                ftp = null;
                return ftp;
            }
            if (FTPReply.isPositiveCompletion(ftp.sendCommand("OPTS UTF8", "ON"))) {
                // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
                LOCAL_CHARSET = "UTF-8";
                LOGGER.info("根据服务端响应设置,本地编码格式为:{}",LOCAL_CHARSET);
            }
            
            //used so server timeout exception will not rise
            ftp.sendNoOp();

            // 编码位置
            //ftp.setControlEncoding("GBK");
            //ftp.setControlEncoding("utf-8");
            ftp.setControlEncoding(LOCAL_CHARSET);
            LOGGER.info("当前设置的本地编码格式为:{}",LOCAL_CHARSET);
            LOGGER.info("当前设置的服务端编码格式为:{}",SERVER_CHARSET);
            
            ftp.setDataTimeout(6000000);
            ftp.setBufferSize(1024);
            // 响应信息
            int replyCode = ftp.getReplyCode();
            if ((!FTPReply.isPositiveCompletion(replyCode))) {
                // 关闭Ftp连接
                closeFTPClient();
                // 释放空间
                ftp = null;
                throw new Exception("登录FTP服务器失败,请检查![Server:" + server + "、" + "User:" + uname + "、" + "Password:" + password);
            } else {
                //FTPClient.enterLocalPassiveMode();要写在 ftpClient.setFileType(FTP.BINARY_FILE_TYPE);  的前面否则虽然不报错但文件上传后依然一直报错
                //设置被动模式,每次数据连接之前，ftp client告诉ftp server开通一个端口来传输数据
                ftp.enterLocalPassiveMode();
                // 文件类型,默认是ASCII
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                //ftp.setCharset(Charset.forName("GBK"));
                // ftp.enterRemotePassiveMode();
                return ftp;
            }
        } catch (Exception e) {
            ftp.disconnect();
            ftp = null;
            throw e;
        }
    }

    /**
     * 配置FTP连接参数
     * 
     * @return
     * @throws Exception
     */
    public FTPClientConfig getFTPClientConfig() throws Exception {
        String systemKey = FTPClientConfig.SYST_NT;
        String serverLanguageCode = "zh";
        FTPClientConfig conf = new FTPClientConfig(systemKey);
        conf.setServerLanguageCode(serverLanguageCode);
        conf.setDefaultDateFormatStr("yyyy-MM-dd");
        return conf;
    }

    /**
     * 向FTP根目录上传文件
     * 
     * @param localFile
     * @param newName
     *            新文件名
     * @throws Exception
     */
    private Boolean uploadFile(String localFile, String newName) throws Exception {
        InputStream input = null;
        boolean success = false;
        try {
            File file = null;
            if (checkFileExist(localFile)) {
                file = new File(localFile);
            }
            input = new FileInputStream(file);
            success = ftp.storeFile(newName, input);
            if (!success) {
                throw new Exception("文件上传失败!");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return success;
    }

    /**
     * 向FTP根目录上传文件
     * 
     * @param input
     * @param newName
     *            新文件名
     * @throws Exception
     */
    private Boolean uploadFile(InputStream input, String newName) throws Exception {
        boolean success = false;
        try {
            success = ftp.storeFile(newName, input);
            if (!success) {
                throw new Exception("文件上传失败!");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return success;
    }

    /**
     * 向FTP指定路径上传文件
     * 
     * @param localFile
     * @param newName
     *            新文件名
     * @param remoteFoldPath
     * @throws Exception
     */
    public Boolean uploadFile(String localFile, String newName, String remoteFoldPath) throws Exception {
        InputStream input = null;
        boolean success = false;
        //localFile=new String(localFile.getBytes("UTF-8"),"iso-8859-1");
//        newName=new String(newName.getBytes("UTF-8"),SERVER_CHARSET);
//        remoteFoldPath=new String(remoteFoldPath.getBytes("UTF-8"),SERVER_CHARSET);
        newName=new String(newName.getBytes(LOCAL_CHARSET),SERVER_CHARSET);
        remoteFoldPath=new String(remoteFoldPath.getBytes(LOCAL_CHARSET),SERVER_CHARSET);
        try {
            File file = null;
            if (checkFileExist(localFile)) {
                file = new File(localFile);
            }
            LOGGER.info("文件上传localFile:{}",localFile);
            LOGGER.info("文件上传remoteFoldPath:{}",remoteFoldPath);
            input = new FileInputStream(file);

            // 改变当前路径到指定路径
            if (!this.changeDirectory(remoteFoldPath)) {
                LOGGER.info("服务器路径不存!");
                //ftp.makeDirectory(remoteFoldPath);
                //ftp.changeWorkingDirectory(remoteFoldPath);
                return false;
            }
            
            //LOGGER.info("文件上传newName:{}", new String(newName.getBytes("ISO-8859-1"), Charset.forName("utf-8")));
            //success = ftp.storeFile(newName, input);
            LOGGER.info("文件上传newName:{}",new String(newName.getBytes(SERVER_CHARSET), Charset.forName(LOCAL_CHARSET)));
            success = ftp.storeFile(newName, input);
            if (!success) {
                throw new Exception("文件上传失败!");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return success;
    }

    /**
     * 向FTP指定路径上传文件
     * 
     * @param input
     * @param newName
     *            新文件名
     * @param remoteFoldPath
     * @throws Exception
     */
    public Boolean uploadFile(InputStream input, String newName, String remoteFoldPath) throws Exception {
        boolean success = false;
        try {
            // 改变当前路径到指定路径
            if (!this.changeDirectory(remoteFoldPath)) {
                ftp.makeDirectory(remoteFoldPath);
                ftp.changeWorkingDirectory(remoteFoldPath);
            }
            success = ftp.storeFile(newName, input);
            if (!success) {
                throw new Exception("文件上传失败!");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return success;
    }

    /**
     * 从FTP服务器下载文件
     * 
     * @param remotePath
     *            FTP路径(不包含文件名)
     * @param fileName
     *            下载文件名
     * @param localPath
     *            本地路径
     */
    public Boolean downloadFile(String remotePath, String fileName, String localPath) throws Exception {

        BufferedOutputStream output = null;
        boolean success = false;
        try {
            // 检查本地路径
            this.checkFileExist(localPath);
            // 改变工作路径
            if (!this.changeDirectory(remotePath)) {
                LOGGER.error("---------服务器路径不存在" + remotePath);
                return false;
            }
            // 列出当前工作路径下的文件列表
            List<FTPFile> fileList = this.getFileList();
            if (fileList == null || fileList.size() == 0) {
                LOGGER.error("---------服务器当前路径下不存在文件！" + fileName);
                return success;
            }
            for (FTPFile ftpfile : fileList) {
                if (ftpfile.getName().equals(fileName)) {
                    File localFilePath = new File(localPath + File.separator + ftpfile.getName());
                    output = new BufferedOutputStream(new FileOutputStream(localFilePath));
                    success = ftp.retrieveFile(ftpfile.getName(), output);
                }
            }
            if (!success) {
                throw new Exception("文件下载失败!");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (output != null) {
                output.close();
            }
        }
        return success;
    }

    /**
     * 从FTP服务器下载文件 zhengxin
     * 
     * @param remotePath
     *            FTP路径(不包含文件名)
     * @param fileName
     *            下载文件名
     * @param localPath
     *            本地路径
     */
    public Boolean downloadFileFromFtp(String remotePath, String fileName, String localPath) throws Exception {

        BufferedOutputStream output = null;
        boolean success = false;
        try {
            // 检查本地路径
            this.checkFileExist(localPath);
            // 改变工作路径
            if (!this.changeDirectory(remotePath)) {
                LOGGER.error("---------服务器路径不存在" + remotePath);
                return false;
            }
            // 列出当前工作路径下的文件列表
            List<FTPFile> fileList = this.getFileList();
            if (fileList == null || fileList.size() == 0) {
                LOGGER.error("---------服务器当前路径下不存在文件！" + fileName);
                return success;
            }
            for (FTPFile ftpfile : fileList) {
                if (ftpfile.getName().equals(fileName + ".pdf")) {
                    File localFilePath = new File(localPath + File.separator
                    // + MD5.MD5Encode(fileName)+".pdf");
                            + MD5EncryptUtils.getEncryptedPwd(fileName, fileName) + ".pdf");
                    output = new BufferedOutputStream(new FileOutputStream(localFilePath));
                    success = ftp.retrieveFile(ftpfile.getName(), output);
                }
            }
            if (!success) {
                throw new Exception("文件下载失败!");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (output != null) {
                output.close();
            }
            ftp.disconnect();
        }
        return success;
    }

    /**
     * 从FTP服务器获取文件流
     * 
     * @param remoteFilePath
     * @return
     * @throws Exception
     */
    public InputStream downloadFile(String remoteFilePath) throws Exception {

        return ftp.retrieveFileStream(remoteFilePath);
    }

    /**
     * 获取FTP服务器上指定路径下的文件列表
     * 
     * @param filePath
     * @return
     */
    public List<FTPFile> getFtpServerFileList(String remotePath) throws Exception {

        FTPListParseEngine engine = ftp.initiateListParsing(remotePath);
        List<FTPFile> ftpfiles = Arrays.asList(engine.getNext(25));

        return ftpfiles;
    }

    /**
     * 获取FTP服务器上[指定路径]下的文件列表
     * 
     * @param path
     * @return
     * @throws Exception
     */
    public List<FTPFile> getFileList(String remotePath) throws Exception {

        List<FTPFile> ftpfiles = Arrays.asList(ftp.listFiles(remotePath));

        return ftpfiles;
    }

    /**
     * 获取FTP服务器[当前工作路径]下的文件列表
     * 
     * @param path
     * @return
     * @throws Exception
     */
    public List<FTPFile> getFileList() throws Exception {

        List<FTPFile> ftpfiles = Arrays.asList(ftp.listFiles());

        return ftpfiles;
    }

    /**
     * 改变FTP服务器工作路径
     * 
     * @param remoteFoldPath
     */
    public Boolean changeDirectory(String remoteFoldPath) throws Exception {

        return ftp.changeWorkingDirectory(remoteFoldPath);
    }

    /**
     * 删除文件
     * 
     * @param remoteFilePath
     * @return
     * @throws Exception
     */
    public Boolean deleteFtpServerFile(String remoteFilePath) throws Exception {

        return ftp.deleteFile(remoteFilePath);
    }

    /**
     * 创建目录
     * 
     * @param remoteFoldPath
     * @return
     */
    public boolean createFold(String remoteFoldPath) throws Exception {

        boolean flag = ftp.makeDirectory(remoteFoldPath);
        if (!flag) {
            throw new Exception("创建目录失败");
        }
        return false;
    }

    /**
     * 删除目录
     * 
     * @param remoteFoldPath
     * @return
     * @throws Exception
     */
    public boolean deleteFold(String remoteFoldPath) throws Exception {

        return ftp.removeDirectory(remoteFoldPath);
    }

    /**
     * 删除目录以及文件
     * 
     * @param remoteFoldPath
     * @return
     */
    public boolean deleteFoldAndsubFiles(String remoteFoldPath) throws Exception {

        boolean success = false;
        List<FTPFile> list = this.getFileList(remoteFoldPath);
        if (list == null || list.size() == 0) {
            return deleteFold(remoteFoldPath);
        }
        for (FTPFile ftpFile : list) {

            String name = ftpFile.getName();
            if (ftpFile.isDirectory()) {
                success = deleteFoldAndsubFiles(remoteFoldPath + "/" + name);
                if (!success)
                    break;
            } else {
                success = deleteFtpServerFile(remoteFoldPath + "/" + name);
                if (!success)
                    break;
            }
        }
        if (!success)
            return false;
        success = deleteFold(remoteFoldPath);
        return success;
    }

    /**
     * 检查本地路径是否存在
     * 
     * @param filePath
     * @return
     * @throws Exception
     */
    public boolean checkFileExist(String filePath) throws Exception {
        boolean flag = false;
        File file = new File(filePath);
        if (!file.exists()) {
            //file.mkdirs();
            // throw new Exception("本地路径不存在,请检查!");
        } else {
            flag = true;
        }
        return flag;
    }

    /**
     * 关闭FTP连接
     * 
     * @param ftp
     * @throws Exception
     */
    public void closeFTPClient(FTPClient ftp) throws Exception {

        try {
            if (ftp.isConnected())
                ftp.logout();
            ftp.disconnect();
        } catch (Exception e) {
            throw new Exception("关闭FTP服务出错!");
        }
    }

    /**
     * 关闭FTP连接
     * 
     * @throws Exception
     */
    public void closeFTPClient() throws Exception {

        this.closeFTPClient(this.ftp);
    }

    /**
     * Get Attribute Method
     * 
     */
    public FTPClient getFtp() {
        return ftp;
    }

    public String getServer() {
        return server;
    }

    public String getUname() {
        return uname;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    /**
     * Set Attribute Method
     * 
     */
    public void setFtp(FTPClient ftp) {
        this.ftp = ftp;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(int port) {
        this.port = port;
    }

}