package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import io.swagger.annotations.Api;
import org.apache.commons.io.FilenameUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

@Api(tags = "文件上传")
@RestController
@RequestMapping("/admin/product")
public class FileUploadController {

    @Value("${fileServer.url}")
    private String fileUrl;

    //图片上传
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file) throws IOException, MyException {
        String configFile = this.getClass().getResource("/tracker.conf").getFile();
        String path = null;
        if( configFile != null){
            ClientGlobal.init(configFile);
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, null);
            path = storageClient1.upload_appender_file1(file.getBytes(), FilenameUtils.getExtension(file.getOriginalFilename()), null);
            System.out.println(path);
        }
        return Result.ok(fileUrl+path);
    }
}
