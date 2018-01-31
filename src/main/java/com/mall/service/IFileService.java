package com.mall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @ Author: 陌北有棵树
 * @ Date: 2018/1/17 14:41
 * @ Description:
 */
public interface IFileService  {

    String upload(MultipartFile file, String path);
}
