package com.mmall.util;/*
 *  cteate by tao on 2018/3/12.
 */

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


public class FtpUtil {
    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");
    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");

    private String user;
    private String ip;
    private int port;
    private String pwd;
    private FTPClient ftpClient;

    public FtpUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FtpUtil ftpUtil = new FtpUtil(ftpIp, 21, ftpUser, ftpPass);
        logger.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile("imge",fileList);
        logger.info("开始连接ftp服务器,结束上传,上传结果:{}",result);
        return result;
    }
    //ftp上传业务逻辑
    private boolean uploadFile(String romatePath, List<File> fileList) throws IOException {
        FileInputStream fis = null;
        boolean uploadResult = false;
        if (connectServer(ftpIp, 21, ftpUser, ftpPass)) {
            try {
                ftpClient.changeWorkingDirectory(romatePath);
              //  System.out.println(ftpClient.getStatus());
                //System.out.println(ftpClient.printWorkingDirectory());
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for (File file : fileList) {
                    fis = new FileInputStream(file);
                    boolean result = ftpClient.storeFile(file.getName(), fis);
                    // System.out.println(result);
                }
                uploadResult = true;
            } catch (Exception e) {
                logger.error("上传文件异常",e);
                uploadResult = false;
            }finally {
                fis.close();
                ftpClient.disconnect();
            }
        }
        return uploadResult;
    }
    //连接ftp服务器
    private boolean connectServer(String ip,int port, String user, String pwd) {
        ftpClient = new FTPClient();
        boolean isSuccess = false;
        try {
            //连接
            ftpClient.connect(ip);
            //登陆
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.error("连接FTP服务器异常",e);
            return false;
        }
        return isSuccess;
    }




    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
