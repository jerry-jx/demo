package com.example.demo.controller;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jerry-jx on 2017/10/23.
 */
@Slf4j
public class GetDownloadUrlFromS3ByUrl1 {

    static AmazonS3 s3Client;


    /**
     * 保存文件到本地临时文件夹
     *
     * @param workBook
     * @param prefix
     * @param suffix
     * @return
     * @throws IOException
     */
    public static File getSaveFileUrl(SXSSFWorkbook workBook, String prefix, String suffix) throws IOException {
        File tempFileAllUrl = File.createTempFile(prefix, suffix);
        log.info("生成临时文件路径：{}", tempFileAllUrl);

        saveFileToTemp(workBook, tempFileAllUrl.getCanonicalPath());
        return tempFileAllUrl;
    }

    /**
     * 将本地文件上传到s3后生成文件下载路径
     *
     * @param accessKey
     * @param secrectKey
     * @param existingBucketName
     * @param keyName
     * @param tempFileAllUrl
     * @param expirationPeriod
     * @return
     */
    public static URL getUploadToS3FileDownloadUrl(String accessKey, String secrectKey, String existingBucketName, String keyName, File tempFileAllUrl, int expirationPeriod) {
        URL url = null;
        try {
            //初始化
            init(accessKey, secrectKey);
            try {

                uploadObjectMPULowLevel(existingBucketName, keyName + tempFileAllUrl.getName(), tempFileAllUrl.getCanonicalPath());
                log.info("文件上传到S3成功！");
                //获取文件下载路径
                url = getRequestUrl(s3Client, existingBucketName, keyName + tempFileAllUrl.getName(), expirationPeriod);

            } catch (IOException e) {
                log.error("获取临时文件路径异常，{}", e);
            } finally {
                //程序退出时删除临时文件
                tempFileAllUrl.deleteOnExit();
                log.info("临时文件：{}，删除成功", tempFileAllUrl);
            }
        } catch (Exception e) {
            log.error("获取文件下载路径异常，{}", e);
        }
        return url;
    }


    /**
     * 初始化s3对象
     *
     * @param accessKey
     * @param secrectKey
     * @throws Exception
     */
    private static void init(String accessKey, String secrectKey) throws Exception {
        s3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secrectKey));
    }


    /**
     * 上传文件到s3
     *
     * @param existingBucketName
     * @param keyName
     * @param tempFileAllUrl
     */
    private static void uploadObjectMPULowLevel(String existingBucketName, String keyName, String tempFileAllUrl) {
        log.info("S3上传存储路径：{}", keyName);
        List<PartETag> partETags = new ArrayList<>();

        // Step 1: Initialize.
        InitiateMultipartUploadRequest initRequest = new
            InitiateMultipartUploadRequest(existingBucketName, keyName);
        InitiateMultipartUploadResult initResponse =
            s3Client.initiateMultipartUpload(initRequest);

        File file = new File(tempFileAllUrl);
        long contentLength = file.length();
        long partSize = 5242880; // Set part size to 5 MB.

        try {
            // Step 2: Upload parts.
            long filePosition = 0;
            for (int i = 1; filePosition < contentLength; i++) {
                // Last part can be less than 5 MB. Adjust part size.
                partSize = Math.min(partSize, (contentLength - filePosition));

                // Create request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest()
                    .withBucketName(existingBucketName).withKey(keyName)
                    .withUploadId(initResponse.getUploadId()).withPartNumber(i)
                    .withFileOffset(filePosition)
                    .withFile(file)
                    .withPartSize(partSize);

                // Upload part and add response to our list.
                partETags.add(
                    s3Client.uploadPart(uploadRequest).getPartETag());

                filePosition += partSize;
            }

            // Step 3: Complete.
            CompleteMultipartUploadRequest compRequest = new
                CompleteMultipartUploadRequest(
                existingBucketName,
                keyName,
                initResponse.getUploadId(),
                partETags);

            s3Client.completeMultipartUpload(compRequest);
        } catch (Exception e) {
            log.error("s3保存文件异常{}", e);
            s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
                existingBucketName, keyName, initResponse.getUploadId()));
        }


    }

    /**
     * 获取文件s3下载路径
     *
     * @param s3Client
     * @param existingBucketName
     * @param keyName
     * @param expirationPeriod
     * @return
     */
    private static URL getRequestUrl(AmazonS3 s3Client, String existingBucketName, String keyName, int expirationPeriod) {
        //获取一个request
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
            existingBucketName, keyName);
        Format cTime = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH, expirationPeriod);
        Date expirationDate = null;
        try {
            expirationDate = new SimpleDateFormat("yyyy-MM-dd").parse(cTime.format(calendar.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置过期时间
        urlRequest.setExpiration(expirationDate);
        //生成公用的url
        URL url = s3Client.generatePresignedUrl(urlRequest);
        log.info("=======文件URL={}", url);
        if (url == null) {
            log.error("can't get s3 file url!");
        }
        return url;
    }


    /**
     * 保存文件到本地临时文件
     *
     * @param workBook
     * @param tempFileAllUrl
     */
    private static void saveFileToTemp(SXSSFWorkbook workBook, String tempFileAllUrl) {

        try {
            FileOutputStream fos = new FileOutputStream(tempFileAllUrl);
            workBook.write(fos);
            fos.close();
        } catch (IOException e) {
            log.error("写入临时文件异常{}", e);
        }
    }


}
