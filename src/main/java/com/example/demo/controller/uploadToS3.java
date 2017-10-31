package com.example.demo.controller;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;

/**
 * Created by jerry-jx on 2017/10/19.
 */
@Slf4j
public class uploadToS3 {
    private static final String bucketName = "aws-mgt-storage";
    private static final String key            = "proguard.zip";
    private static final String tempFile           = "D:/soft/proguard5.2.zip";
    private static final String accessKey          = "AKIAJ5HKYWQNUZEINUGQ";
    private static final String secrectKey         = "VgeMknpoOQe1D50PQFufGm8TEPu7rhMYo0j4J8DT";

    public static void main (String[] args){
        try {
            System.out.println(uploadToS3());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String uploadToS3() throws Exception {

        //首先创建一个s3的客户端操作对象（需要amazon提供的密钥）
        AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(accessKey,secrectKey));
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);
        //设置bucket,key
        //String key = UUID.randomUUID() + ".apk";
        try {
            //验证名称为bucketName的bucket是否存在，不存在则创建
            if (!checkBucketExists(s3, bucketName)) {
                s3.createBucket(bucketName);
            }
            //上传文件
            s3.putObject(new PutObjectRequest(bucketName, key, tempFile));
            S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
            //获取一个request
            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
                bucketName, key);
            Date expirationDate = null;
            try {
                expirationDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //设置过期时间
            urlRequest.setExpiration(expirationDate);
            //生成公用的url
            URL url = s3.generatePresignedUrl(urlRequest);
            System.out.println("=========URL=================" + url + "============URL=============");
            if (url == null) {
                throw new Exception("can't get s3 file url!");
            }
            return url.toString();
        } catch (AmazonServiceException ase) {
            ase.printStackTrace();
            log.info("====================================AWS S3 UPLOAD ERROR START======================================");
            log.info("Caught an AmazonServiceException, which means your request made it "
                + "to Amazon S3, but was rejected with an error response for some reason.");
            log.info("Caught an AmazonServiceException, which means your request made it "
                + "to Amazon S3, but was rejected with an error response for some reason.");
            log.info("Error Message:    " + ase.getMessage());
            log.info("HTTP Status Code: " + ase.getStatusCode());
            log.info("AWS Error Code:   " + ase.getErrorCode());
            log.info("Error Type:       " + ase.getErrorType());
            log.info("Request ID:       " + ase.getRequestId());
            log.info(ase.getMessage(), ase);
            log.info("====================================AWS S3 UPLOAD ERROR END======================================");
            throw new Exception("error occurs during upload to s3!");
        } catch (AmazonClientException ace) {
            log.info("====================================AWS S3 UPLOAD ERROR START======================================");
            log.info("Caught an AmazonClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with S3, "
                + "such as not being able to access the network.");
            log.info("Error Message: " + ace.getMessage());
            log.info("====================================AWS S3 UPLOAD ERROR END======================================");
            throw new Exception("error occurs during upload to s3!");
        }
    }

    /**
     * 验证s3上是否存在名称为bucketName的Bucket
     * @param s3
     * @param bucketName
     * @return
     */
    public static boolean checkBucketExists (AmazonS3 s3, String bucketName) {
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket bucket : buckets) {
            if (Objects.equals(bucket.getName(), bucketName)) {
                return true;
            }
        }
        return false;
    }
}
