package com.mall.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @ Author: 陌北有棵树
 * @ Date: 2018/1/17 14:56
 * @ Description:
 */
@Slf4j
@Getter
@Setter
public class FTPUtil {



    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }


    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp,21,ftpUser,ftpPass);
        log.info("开始连接FTP服务器");
        boolean result = ftpUtil.uploadFile("img",fileList);
        log.info("开始连接FTP服务器，结束上传，上传结果：{}");
        return result;
    }

    /**
     *@ Author:陌北有棵树
     *@ Date: 2018/1/17 15:01
     *@ Description:
     *@ Param: String remotePath : 远程的路径，ftp服务器在Linux上是一个文件夹，用remotePath设置文件夹下的路径
     *         List<File> fileList : 文件列表
     *@ Return:
     */
    private boolean uploadFile(String remotePath,List<File> fileList)throws IOException{
        boolean uploaded = true;
        FileInputStream fis = null;
        /* 连接FTP服务器 */
        if(connectServer(this.getIp(),this.getPort(),this.getUser(),this.pwd)){
            /* 更改工作目录 */
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                /* 设置缓冲区 */
                ftpClient.setBufferSize(1024);

                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

                /* 打开本地的被动模式 */
                ftpClient.enterLocalPassiveMode();
                /* 上传 */
                for (File fileItem : fileList){
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fis);
                }
            } catch (IOException e) {
                log.error("上传文件异常",e);
                uploaded = false;
                e.printStackTrace();
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
        }

        return uploaded;
    }

    private boolean connectServer(String ip,int port,String user,String pwd){
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user,pwd);
        } catch (IOException e) {
            log.error("连接FTP服务器异常",e);
            e.printStackTrace();
        }
        return isSuccess;
    }


}
