package com.mmall.service;/*
 *  cteate by tao on 2018/3/9.
 */

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    String upload(String path, MultipartFile file);
}
