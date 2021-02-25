package com.pegasus.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by enHui.Chen on 2019/12/19.
 */
@Slf4j
public class FastdfsUtil {
    private static StorageClient1 client = null;

    public static void main(String[] args) throws Exception {
        ClientGlobal.initByProperties("fastdfs-client.properties");
//        uploadFileByPath();
        downloadFile();
//        deleteFile();
    }

    // 上传
    public static void uploadFileByPath() throws Exception {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        client = new StorageClient1(trackerServer, storageServer);
        NameValuePair[] nameValuePairs = new NameValuePair[1];
        nameValuePairs[0] = new NameValuePair("filename", "application");
        String yml = client.upload_file1("E:\\pegasus\\pegasus-back\\src\\main\\resources\\application.yml", "yml", nameValuePairs);
        log.info("输出地址:{}", yml);
    }

    // 下载
    public static void downloadFile() throws Exception {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        client = new StorageClient1(trackerServer, storageServer);
        byte[] bytes = client.download_file("group1", "M00/00/00/wKjRh137KG6AAZVjAABdreSfEnY481.jpg");
        BufferedOutputStream bufferedWriter = new BufferedOutputStream(new FileOutputStream(new File("E:\\pegasus\\pegasus-back\\target\\test1.jpg")));
        bufferedWriter.write(bytes);
        bufferedWriter.close();
    }

    // 删除
    public static void deleteFile() throws Exception {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        client = new StorageClient1(trackerServer, storageServer);
        client.delete_file1("group1/M00/00/00/wKjRh137MAKASe8ZAAAEOftKQFY936.yml");
    }
}
