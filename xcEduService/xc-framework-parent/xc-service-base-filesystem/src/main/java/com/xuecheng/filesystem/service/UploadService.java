package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author hewei
 * @date 2019/11/1 - 19:13
 */
@Service
public class UploadService {
    @Autowired
    FileSystemRepository fileSystemRepository;

    private static final List<String> CONTENT_TYPE = Arrays.asList("image/gif", "image/jpeg");

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata) {
        if(multipartFile ==null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        //第一步：上传图片
        boolean flag = this.image_upload(multipartFile);
        if(flag == false){
            return new UploadFileResult(CommonCode.FAIL,null);
        }
        //第二步：将文件id及其它文件信息存储到mongodb中。
        String originalFilename = multipartFile.getOriginalFilename();

        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(originalFilename);
        fileSystem.setFilePath(originalFilename);
        fileSystem.setFiletag(filetag);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFileName(originalFilename);
        fileSystem.setFileType(multipartFile.getContentType());
        if(StringUtils.isNotBlank(metadata)){
            Map map = JSON.parseObject(metadata, Map.class);
            fileSystem.setMetadata(map);
        }
        fileSystemRepository.save(fileSystem);

        return new UploadFileResult(CommonCode.SUCCESS,fileSystem);
    }

    //上传图片
    private boolean image_upload(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        //校验文件类型
        String contentType = multipartFile.getContentType();
        if(!CONTENT_TYPE.contains(contentType)){
            LOGGER.info("文件类型不合法: {}", originalFilename);
            return false;
        }
        //校验文件内容
        try {
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
            if (bufferedImage == null) {
                LOGGER.info("文件内容不合法：{}", originalFilename);
                return false;
            }
            //保存到服务器       没用fastDFS,就存在本地玩
            multipartFile.transferTo(new File("E:\\upload\\xuecheng_images\\" + originalFilename));
            return true;
        } catch (IOException e) {
            LOGGER.info("服务器内部错误: {}", originalFilename);
            e.printStackTrace();
        }
        return false;
    }
}
