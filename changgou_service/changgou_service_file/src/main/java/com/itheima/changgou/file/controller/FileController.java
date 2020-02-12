package com.itheima.changgou.file.controller;

import com.itheima.changgou.costant.MessageConstants;
import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.file.util.FastDFSUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    @RequestMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        String path = FastDFSUtils.uploadFile(file);
        return new Result(true, StatusCode.OK, MessageConstants.FILE_UPLOAD_SUCCESS, path);
    }
}
