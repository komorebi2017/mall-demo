package com.mall.service.impl;

import com.google.common.collect.Lists;
import com.mall.service.IFileService;
import com.mall.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


/**
 * @ Author: 陌北有棵树
 * @ Date: 2018/1/17 14:41
 * @ Description:
 */
@Service("iFileService")
@Slf4j
public class FileServiceImpl implements IFileService {



    public String upload(MultipartFile file,String path){
        /* 拿到上传文件的原始文件名 */
        String filename = file.getOriginalFilename();
        /* 获取扩展名 */
        String fileExtensionName = filename.substring(filename.lastIndexOf(".")+1);
        /* 上传文件的名字,防止重名覆盖*/
        String uploadFilename = UUID.randomUUID().toString()+"."+fileExtensionName;

        log.info("开始上传文件，上传的文件名:{},上传的路径:{},新文件名:{}",filename,path,uploadFilename);

        /* 声明目录的FIle */
        File fileDir = new File(path);
        if(!fileDir.exists()){
            /* 使它有权限 */
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFilename);

        try {
            file.transferTo(targetFile);
            /* 文件已经上传成功，传到upload文件夹下了*/

            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            /* 执行完说明已经上传到FTP服务器上 */

            /* 上传完成后，删除upload下面的文件*/
            targetFile.delete();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return targetFile.getName();


    }

}
