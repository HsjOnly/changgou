package com.itheima.changgou.file.util;

import com.itheima.changgou.file.controller.FileController;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FastDFSUtils {


    static {
        try {
            ClientGlobal.init(FileController.class.getClassLoader().getResource("fdfs_client.conf").getFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 此方法用于上传文件，并返回文件存储的路径
     * 包名: com.itheima.changgou.file.util
     * 作者: Narcissu
     * 日期：2019/12/30 15:10
     */
    public static String uploadFile(MultipartFile file) throws Exception {
        StorageClient storageClient = getStorageClient();
        // 从最后一个.后一个字符开始截取文件名，获取扩展名
        String fileExtentName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        // 上传文件
        String[] strings = storageClient.upload_file(file.getBytes(), fileExtentName, null);
        // 存储结点中的的文件路径（存储结点+路径—）
        String path = strings[0] + "/" + strings[1];
        // 完整的访问路径（Tracker路径+存储结点中的路径）
        String trackerUrl = getTrackerUrl();
        return trackerUrl + "/" + path;
    }

    /*
     * 删除文件
     * 包名: com.itheima.changgou.file.util
     * 作者: Narcissu
     * 日期：2019/12/30 15:27
     */
    public static String deleteFile(String groupName, String remoteFileName) throws Exception {
        StorageClient storageClient = getStorageClient();
        int deleteResult = storageClient.delete_file(groupName, remoteFileName);
        if (deleteResult == 0) {
            return "删除成功";
        } else {
            return "删除失败";
        }
    }

    /*
     * 下载
     * 包名: com.itheima.changgou.file.util
     * 作者: Narcissu
     * 日期：2019/12/30 15:27
     */
    public static byte[] downloadFile(String groupName, String remoteFileName) throws Exception {
        StorageClient storageClient = getStorageClient();
        byte[] file = storageClient.download_file(groupName, remoteFileName);
        return file;
    }


    private static StorageClient getStorageClient() throws IOException {
        // 获取TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        // 获取TrackerServer
        TrackerServer trackerServer = trackerClient.getConnection();
        // 获取StorageClient
        return new StorageClient(trackerServer, null);
    }

    private static String getTrackerUrl() throws Exception {
        // 获取TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        // 获取TrackerServer
        TrackerServer trackerServer = trackerClient.getConnection();
        // 获取Nginx的IP地址及端口号并返回
        String ipAddress = trackerServer.getInetSocketAddress().getHostString();
        int trackerPort = ClientGlobal.getG_tracker_http_port();
        return ipAddress + ":" + trackerPort;
    }
}
