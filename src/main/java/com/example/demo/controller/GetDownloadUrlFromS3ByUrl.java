package com.example.demo.controller;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by jerry-jx on 2017/10/23.
 */
@Slf4j
public class GetDownloadUrlFromS3ByUrl {

    static AmazonS3 s3Client;

    public static void main(String[] args) throws Exception {

        String existingBucketName = "aws-mgt-storage";
        String keyName = "reports/";
        // private static final String filePath = "D:/soft/PMD-Intellij.zip";//https://download-cf.jetbrains.com/idea/ideaIU-2017.2.5.exe
        String accessKey = "AKIAJ5HKYWQNUZEINUGQ";
        String secrectKey = "VgeMknpoOQe1D50PQFufGm8TEPu7rhMYo0j4J8DT";
        int expirationPeriod = 7;
        String requestUrl = "https://twistedsifter.files.wordpress.com/2016/07/dulmen_bornste_waldweg.jpg";
            /**
             * https://download.moodle.org/download.php/stable33/moodle-latest-33.tgz
             * http://www.freefileviewer.com/downloads/newest.exe
             * http://i1-news.softpedia-static.com/images/news2/Picture-of-the-Day-Real-Life-Simba-and-Mufasa-Caught-on-Camera-in-Tanzania-392687-2.jpg
             */
        getDownloadUrl(accessKey, secrectKey, existingBucketName, keyName,  requestUrl,"GET",expirationPeriod);
    }

    public static URL getDownloadUrl(String accessKey, String secrectKey, String existingBucketName, String keyName, String requestUrl,String requestMethod,int expirationPeriod) {
        try {
            //初始化
            init(accessKey, secrectKey);

            String prefix = requestUrl.substring(requestUrl.lastIndexOf("/") + 1, requestUrl.lastIndexOf("."));
            String suffix = requestUrl.substring(requestUrl.lastIndexOf("."));

            //创建临时文件
            File tempFileAllUrl = null;
            try {
                tempFileAllUrl = File.createTempFile(prefix, suffix);

                log.info("生成临时文件路径：{}", tempFileAllUrl);
                Boolean saveResult = saveUrlAs(requestUrl, tempFileAllUrl, requestMethod);
                if (saveResult) {
                    uploadObjectMPULowLevel(existingBucketName, keyName + tempFileAllUrl.getName(), tempFileAllUrl.getCanonicalPath());
                    log.info("文件上传到S3成功！");
                    //获取文件下载路径
                     return getRequestUrl(s3Client, existingBucketName, keyName +  tempFileAllUrl.getName(), expirationPeriod);
                } else {
                    log.error("文件上传到S3失败！");
                }
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
        return null;
    }


    private static void init(String accessKey, String secrectKey) throws Exception {
        s3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secrectKey));
    }

    public static void uploadObjectMPULowLevel(String existingBucketName, String keyName, String tempFileAllUrl) {
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
            e.printStackTrace();
            s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
                existingBucketName, keyName, initResponse.getUploadId()));
        }
    }

    /**
     * 获取文件下载路径
     */
    public static URL getRequestUrl(AmazonS3 s3Client, String existingBucketName, String keyName, int expirationPeriod) {
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
     * @param url 请求的路径
     * @param method 请求方法，包括POST和GET
     * @功能 下载临时素材接口
     */

    public static Boolean saveUrlAs(String url, File tempFileAllUrl, String method) {
        //System.out.println("fileName---->"+filePath);
      /*  //创建不同的文件夹目录
        File file=new File(filePath);
        //判断文件夹是否存在
        if (!file.exists())
        {
            //如果文件夹不存在，则创建新的的文件夹
            file.mkdirs();
        }*/
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            // 建立链接
            URL httpUrl = new URL(url);
            conn = (HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
           /* //判断文件的保存路径后面是否以/结尾
            if (!filePath.endsWith("/")) {

                filePath += "/";

            }*/
            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
            fileOut = new FileOutputStream(tempFileAllUrl);
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);

            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            //保存文件
            while (length != -1) {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("抛出异常！！");
        }

        return true;

    }
}
