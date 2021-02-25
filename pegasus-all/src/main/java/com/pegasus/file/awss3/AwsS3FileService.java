package com.pegasus.file.awss3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.pegasus.common.exception.CommonException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;

/**
 * Created by enHui.Chen on 2020/8/26.
 */
@Slf4j
public class AwsS3FileService {
    public static final String REGION = "sh-1";
    public static final String END_POINT = "http://192.168.91.128:7480";
    public static final String ACCESS_KEY = "Z664M1KZ6I1WAVWA5CAQ";
    public static final String SECRET_KEY = "tkStCEZBLQNZt0b3sLOcJTvArqHb28QwKxV0ijMo";
    public static AmazonS3 connection;

    static {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);

        AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration(END_POINT, REGION);
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);

        connection = AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfig)
                .withClientConfiguration(clientConfig)
                .withCredentials(credentialsProvider)
                .disableChunkedEncoding()
                .withPathStyleAccessEnabled(true).build();
    }

    public static void main(String[] args) throws Exception {
        uploadFileByLocalPath("bucket-demo", "test", "C:\\Users\\Administrator\\Desktop\\1605092659(1).png");
//        uploadFileByLocalPath("bucket-demo", "test2", "C:\\Users\\Administrator\\Desktop\\1605093311(1).png");
//        deleteFileByFileKey("bucket-demo", "test");
        byte[] tests = downloadFileByFileKey("bucket-demo", "test");
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\test.png");
        fileOutputStream.write(tests);
        fileOutputStream.close();
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取连接
     * @Data 2020/11/11
     */
    public static AmazonS3 getConnection() {
        return connection;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取桶
     * @Data 2020/11/11
     */
    private static List<Bucket> getBuckets() {
        return getConnection().listBuckets();
    }

    /**
     * @Author: enHui.Chen
     * @Description: 创建桶
     * @Data 2020/11/11
     */
    private static Bucket createBucket(String bucketName) {
        return getConnection().createBucket(bucketName);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 删除桶
     * @Data 2020/11/11
     */
    private static void deleteBucket(String bucketName) {
        getConnection().deleteBucket(bucketName);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取桶对象
     * @Data 2020/11/11
     */
    private static ObjectListing getBucketObjects(String bucketName) {
        return getConnection().listObjects(bucketName);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 创建桶对象
     * @Data 2020/11/11
     */
    private static PutObjectResult createObject(String bucketName, String fileKey, ByteArrayInputStream input, String fileType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(fileType);
        return getConnection().putObject(bucketName, fileKey, input, objectMetadata);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取桶对象
     * @Data 2020/11/11
     */
    private static S3Object getObjectByFileKey(String bucketName, String fileKey) {
        return getConnection().getObject(bucketName, fileKey);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 删除桶对象
     * @Data 2020/11/11
     */
    private static void deleteObjectByFileKey(String bucketName, String fileKey) {
        getConnection().deleteObject(bucketName, fileKey);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 文件上传
     * @Data 2020/11/11
     */
    private static void uploadFileByLocalPath(String bucketName, String fileKey, String localPath) {
        File file = new File(localPath);

        // get file type
        String name = file.getName();
        String fileType = name.substring(name.lastIndexOf("."));

        // get file bytes
        ByteArrayInputStream input;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            input = new ByteArrayInputStream(getBytes(fileInputStream));
        } catch (IOException e) {
            log.error("uploadFile error:", e);
            throw new CommonException(e);
        }
        PutObjectResult object = createObject(bucketName, fileKey, input, fileType);
        System.out.println();
    }

    /**
     * @Author: enHui.Chen
     * @Description: 文件下载
     * @Data 2020/11/11
     */
    private static byte[] downloadFileByFileKey(String bucketName, String fileKey) {
        S3Object fileObj = getObjectByFileKey(bucketName, fileKey);
        byte[] bytes;
        try {
            bytes = getBytes(fileObj.getObjectContent());
        } catch (IOException e) {
            log.error("downloadFile error:", e);
            throw new CommonException(e);
        }
        return bytes;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 文件删除
     * @Data 2020/11/11
     */
    private static void deleteFileByFileKey(String bucketName, String fileKey) {
        deleteObjectByFileKey(bucketName, fileKey);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 输入流转字节数组
     * @Data 2020/11/11
     */
    private static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = inputStream.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
