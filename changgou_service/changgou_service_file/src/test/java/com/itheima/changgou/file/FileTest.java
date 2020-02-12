package com.itheima.changgou.file;

import org.apache.ibatis.annotations.Delete;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileTest {

    @Test
    public void upload() throws Exception {
        ClientGlobal.init(FileTest.class.getClassLoader().getResource("fdfs_client.conf").getFile());

        // 创建TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        // 从服务器获取连接
        TrackerServer trackerServer = trackerClient.getConnection();
        // 创建StorageClient
        StorageClient storageClient = new StorageClient(trackerServer, null);
        String[] path = storageClient.upload_appender_file("d:/8b12aa9d618467741ed3289b6cb91501.jpeg", "jpeg", null);
        for (String s : path) {
            System.out.println(s);
        }
    }

    @Test
    public void download() throws Exception {
        ClientGlobal.init(FileTest.class.getClassLoader().getResource("fdfs_client.conf").getFile());

        // 创建TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        // 从服务器获取连接
        TrackerServer trackerServer = trackerClient.getConnection();
        // 创建StorageClient
        StorageClient storageClient = new StorageClient(trackerServer, null);

        // 从storage读取文件到内存
        byte[] file = storageClient.download_file("group1", "M00/00/00/wKjThF4Io6OAMvLcAAAXkpvx33A628.jpg");
        // 将文件写到D盘
        FileOutputStream fileOutputStream = new FileOutputStream("d:/111.jpeg");
        fileOutputStream.write(file);
        fileOutputStream.close();
    }

    @Test
    public void delete() throws Exception {
        ClientGlobal.init(FileTest.class.getClassLoader().getResource("fdfs_client.conf").getFile());

        // 创建TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        // 从服务器获取连接
        TrackerServer trackerServer = trackerClient.getConnection();
        // 创建StorageClient
        StorageClient storageClient = new StorageClient(trackerServer, null);

        // 从storage读取文件到内存
        int result = storageClient.delete_file("group1", "M00/00/00/wKjThF4IlWCEI7SPAAAAAMN9jGk62.jpeg");
        System.out.println(result);
    }

}
