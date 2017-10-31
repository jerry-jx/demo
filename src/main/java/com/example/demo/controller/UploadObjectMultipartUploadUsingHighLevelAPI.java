package com.example.demo.controller;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.transfer.Download;
import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadObjectMultipartUploadUsingHighLevelAPI {

    public static void main(String[] args) throws Exception {
        String existingBucketName = "aws-mgt-storage";
        String keyName            = "reports/common-encrypt.zip";
        String filePath           = "D:/soft/common-encrypt.zip";
        String accessKey          = "AKIAJ5HKYWQNUZEINUGQ";
        String secrectKey         = "VgeMknpoOQe1D50PQFufGm8TEPu7rhMYo0j4J8DT";

        TransferManager tm = new TransferManager(new BasicAWSCredentials(accessKey,secrectKey));
        System.out.println("Hello");
        // TransferManager processes all transfers asynchronously, 
        // so this call will return immediately.
        Upload upload = tm.upload(
            existingBucketName, keyName, new File(filePath));
        System.out.println("Hello2");

        try {
            // Or you can block and wait for the upload to finish
            upload.waitForCompletion();
            System.out.println("Upload complete.");

            //获取一个request
            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
                existingBucketName, keyName);
            Date expirationDate = null;
            try {
                expirationDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-10-25");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //设置过期时间
            urlRequest.setExpiration(expirationDate);
            //生成公用的url
            Download url = tm.download(existingBucketName,keyName, new File("D:/soft"));
            tm.abortMultipartUploads(existingBucketName,new Date());
            System.out.println("=========URL=================" + url + "============URL=============");
        } catch (AmazonClientException amazonClientException) {
            System.out.println("Unable to upload file, upload was aborted.");
            amazonClientException.printStackTrace();
        }
    }
}