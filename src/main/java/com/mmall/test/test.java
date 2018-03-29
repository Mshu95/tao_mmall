package com.mmall.test;/*
 *  cteate by tao on 2018/3/12.
 */

import com.mmall.util.PropertiesUtil;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

public class test {
    public static void main(String[] args) {
       FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect("192.168.3.180");
           boolean result = ftpClient.login("ftpuser","123");
            System.out.println(result);
            System.out.println(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com111/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
