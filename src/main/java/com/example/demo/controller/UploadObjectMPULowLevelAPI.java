package com.example.demo.controller;

import static com.example.demo.controller.DownloadFile.saveUrlAs;

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
import com.amazonaws.services.s3.transfer.TransferManager;
import java.io.File;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UploadObjectMPULowLevelAPI {


    static AmazonS3 s3Client;
    static TransferManager tx;
    private static  String existingBucketName = "aws-mgt-storage";
    private static  String keyName = "reports/";
   // private static final String filePath = "D:/soft/PMD-Intellij.zip";//https://download-cf.jetbrains.com/idea/ideaIU-2017.2.5.exe
    private static  String accessKey = "AKIAJ5HKYWQNUZEINUGQ";
    private static  String secrectKey = "VgeMknpoOQe1D50PQFufGm8TEPu7rhMYo0j4J8DT";
    private static  int expirationPeriod = 7;

    public static void main(String[] args) throws Exception {


        init(accessKey, secrectKey);
        String photoUrl = "http://cdn.newsapi.com.au/image/v1/9fdbf585d17c95f7a31ccacdb6466af9";
        String fileName = photoUrl.substring(photoUrl.lastIndexOf("/")+1);
        String file = "D:/";
        String filePath = saveUrlAs(photoUrl, file ,fileName,"GET");
        System.out.println("Run ok!/n<BR>Get URL file " + filePath);

        UploadObjectMPULowLevel(existingBucketName, keyName+fileName, filePath);
        //获取文件下载路径
        URL url = getRequestUrl(s3Client, existingBucketName, keyName+fileName, expirationPeriod);

    }


    private static void init(String accessKey, String secrectKey) throws Exception {
       /* AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Exception on credentials.", e);
        }*/
        s3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secrectKey));
        //Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        //s3.setRegion(usWest2);
        tx = new TransferManager(s3Client);
    }

    public static void UploadObjectMPULowLevel(String existingBucketName, String keyName, String filePath) {
       /* AmazonS3 s3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secrectKey));*/

        // Create a list of UploadPartResponse objects. You get one of these
        // for each part upload.
        List<PartETag> partETags = new ArrayList<>();

        // Step 1: Initialize.
        InitiateMultipartUploadRequest initRequest = new
            InitiateMultipartUploadRequest(existingBucketName, keyName);
        InitiateMultipartUploadResult initResponse =
            s3Client.initiateMultipartUpload(initRequest);

        File file = new File(filePath);
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
        Format ctime = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH, expirationPeriod);
        Date expirationDate = null;
        try {
            expirationDate = new SimpleDateFormat("yyyy-MM-dd").parse(ctime.format(calendar.getTime()));
            //expirationDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置过期时间
        urlRequest.setExpiration(expirationDate);
        //生成公用的url
        URL url = s3Client.generatePresignedUrl(urlRequest);
        log.info("=========URL={}", url);
        if (url == null) {
            log.error("can't get s3 file url!");
        }
        return url;
    }

}
