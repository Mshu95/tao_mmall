package com.mmall.service.impl;/*
 *  cteate by tao on 2018/3/9.
 */

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FtpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    public String upload(String path, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + extensionName;
        logger.info("开始上传文件，上传文件名{}，上传路径{}，新文件名{}",fileName,path,uploadFileName);
        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);
        //文件写入upload
        try {
            file.transferTo(targetFile);
            //上传到ftp图片服务器
            FtpUtil.uploadFile(Lists.newArrayList(targetFile));
            //删除tomcat缓存文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常",e);
            e.printStackTrace();
            return null;
        }
        return targetFile.getName();

    }
}
