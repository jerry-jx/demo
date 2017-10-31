/*
package com.example.demo.controller;

*/
/**
 * Created by jerry-jx on 2017/10/21.
 *//*

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.apache.log4j.Logger;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
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
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.customerservicePES.exception.PgCsDocException;
import com.customerservicePES.utils.DateUtil;

*/
/**
 *
 * @author zhangjiamei
 *
 *//*

public class S3Upload {
    private static Logger logger = Logger.getLogger(S3Upload.class);
    private static String AWS_ACCESS_KEY = "AKIAP5754BDN2Q35RLNQ";
    private static String AWS_SECRET_KEY = "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";
    private static String bucket_name = "elsa-project/dev";
    public static AmazonS3 s3;
    */
/**
     *
     * @param file_path
     * @param bucket_name
     * @param key_prefix
     * @param pause
     *//*

    @SuppressWarnings("deprecation")
    public static void uploadFile(String file_path, String bucket_name,
        String key_prefix, boolean pause)
    {
        System.out.println("file: " + file_path +
            (pause ? " (pause)" : ""));

        String key_name = null;
        if (key_prefix != null) {
            key_name = key_prefix + '/' + file_path;
        } else {
            key_name = file_path;
        }

        File f = new File(file_path);
        AWSCredentials credentials = null;
        credentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);
        s3 = new AmazonS3Client(credentials);
        Region usWest2 = Region.getRegion(Regions.CN_NORTH_1);
        s3.setRegion(usWest2);
        TransferManager xfer_mgr = new TransferManager(s3);
        try {
            Upload xfer = xfer_mgr.upload(bucket_name, key_name, f);
            // loop with Transfer.isDone()
            XferMgrProgress.showTransferProgress(xfer);
            //  or block with Transfer.waitForCompletion()
            XferMgrProgress.waitForCompletion(xfer);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        xfer_mgr.shutdownNow();
    }
    */
/**
     *
     * @param tempFile
     * @param remoteFileName
     * @return
     * @throws IOException
     *//*

    @SuppressWarnings({ "deprecation", "unused" })
    public static String uploadToS3(File tempFile, String remoteFileName) throws IOException {
        //首先创建一个s3的客户端操作对象（需要amazon提供的密钥）
        s3 = new AmazonS3Client(new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY));
        Region usWest2 = Region.getRegion(Regions.CN_NORTH_1);
        s3.setRegion(usWest2);
        //设置bucket,key
        String key = UUID.randomUUID() + ".pg";
        logger.info("key : " + key );
        try {
            //验证名称为bucketName的bucket是否存在，不存在则创建
//          if (!checkBucketExists(s3, bucket_name)) {
//              s3.createBucket(bucket_name);
//          }
            //上传文件
//          s3.putObject(new PutObjectRequest(bucket_name, key, tempFile));

            S3Object s3Object = s3.getObject(new GetObjectRequest(bucket_name, key));
            //获取一个request
            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucket_name, key);
            */
/**
             * 设置过期时间
             *//*

//          Date expirationDate = null;
//          try {
//              expirationDate = DateUtil.yyyyMMdd.parse("2020-12-31");
//          } catch (Exception e) {
//              e.printStackTrace();
//          }
//          urlRequest.setExpiration(expirationDate);
            //生成公用的url
            URL url = s3.generatePresignedUrl(urlRequest);
            System.out.println("=========URL=================" + url + "============URL=============");
            if (url == null) {
                throw new AmazonClientException("can't get s3 file url!");
            }
            return url.toString();
        } catch (AmazonServiceException ase) {
            ase.printStackTrace();
            logger.info("====================================AWS S3 UPLOAD ERROR START======================================");
            logger.info("Caught an AmazonServiceException, which means your request made it "
                + "to Amazon S3, but was rejected with an error response for some reason.");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
            logger.info(ase.getMessage(), ase);
            logger.info("====================================AWS S3 UPLOAD ERROR END======================================");
            throw new AmazonClientException("error occurs during upload to s3!");
        } catch (AmazonClientException ace) {
            ace.printStackTrace();
            logger.info("====================================AWS S3 UPLOAD ERROR START======================================");
            logger.info("Caught an AmazonClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with S3, "
                + "such as not being able to access the network.");
            logger.info("Error Message: " + ace.getMessage());
            logger.info("====================================AWS S3 UPLOAD ERROR END======================================");
            throw new AmazonClientException("error occurs during upload to s3!");
        }
    }
    */
/**
     *
     * @param inputStream
     * @param key
     * @return
     * @throws IOException
     *//*

    @SuppressWarnings({ "deprecation", "unused" })
    public static String uploadToS3(InputStream inputStream, String key) throws IOException {
        System.out.println("key: " + key);
        //首先创建一个s3的客户端操作对象（需要amazon提供的密钥）
        s3 = new AmazonS3Client(new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY));
        Region usWest2 = Region.getRegion(Regions.CN_NORTH_1);
        s3.setRegion(usWest2);
        try {
            //验证名称为bucketName的bucket是否存在，不存在则创建
//          if (!checkBucketExists(s3, bucket_name)) {
//              s3.createBucket(bucket_name);
//          }
            //上传文件
            s3.putObject(bucket_name, key, inputStream, null);

            S3Object s3Object = s3.getObject(new GetObjectRequest(bucket_name, key));
            //获取一个request
            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucket_name, key);
            */
/**
             * 设置过期时间
             *//*

            urlRequest.setExpiration(DateUtil.getDateByPeriod(new Date(), 7));
            //生成公用的url
            URL url = s3.generatePresignedUrl(urlRequest);
            System.out.println("=========URL=================" + url + "============URL=============");
            if (url == null) {
                throw new AmazonClientException("can't get s3 file url!");
            }
            return url.toString();
        } catch (AmazonServiceException ase) {
            ase.printStackTrace();
            logger.info("====================================AWS S3 UPLOAD ERROR START======================================");
            logger.info("Caught an AmazonServiceException, which means your request made it "
                + "to Amazon S3, but was rejected with an error response for some reason.");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
            logger.info(ase.getMessage(), ase);
            logger.info("====================================AWS S3 UPLOAD ERROR END======================================");
            throw new AmazonClientException("error occurs during upload to s3!");
        } catch (AmazonClientException ace) {
            ace.printStackTrace();
            logger.info("====================================AWS S3 UPLOAD ERROR START======================================");
            logger.info("Caught an AmazonClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with S3, "
                + "such as not being able to access the network.");
            logger.info("Error Message: " + ace.getMessage());
            logger.info("====================================AWS S3 UPLOAD ERROR END======================================");
            throw new AmazonClientException("error occurs during upload to s3!");
        }
    }
    */
/**
     *
     * @param tempFile
     * @return
     * @throws IOException
     *//*

    @SuppressWarnings({ "deprecation", "unused" })
    public static String uploadToS3(File tempFile) throws IOException{
        //首先创建一个s3的客户端操作对象（需要amazon提供的密钥）
        s3 = new AmazonS3Client(new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY));
        Region usWest2 = Region.getRegion(Regions.CN_NORTH_1);
        s3.setRegion(usWest2);
        //设置bucket,key
        String key = UUID.randomUUID() + ".pg";
        logger.info("key : " + key );
        try {
            //验证名称为bucketName的bucket是否存在，不存在则创建
//          if (!checkBucketExists(s3, bucket_name)) {
//              s3.createBucket(bucket_name);
//          }
            //上传文件
            s3.putObject(new PutObjectRequest(bucket_name, key, tempFile));
            S3Object object = s3.getObject(new GetObjectRequest(bucket_name, key));
            //获取一个request
            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucket_name, key);
            Date expirationDate = null;
            try {
                expirationDate = DateUtil.yyyyMMdd.parse("2020-12-31");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //设置过期时间
//          urlRequest.setExpiration(expirationDate);
            //生成公用的url
            URL url = s3.generatePresignedUrl(urlRequest);
            System.out.println("=========URL=================" + url + "============URL=============");
            if (url == null) {
                throw new AmazonClientException("can't get s3 file url!");
            }
            return url.toString();
        } catch (AmazonServiceException ase) {
            ase.printStackTrace();
            logger.info("====================================AWS S3 UPLOAD ERROR START======================================");
            logger.info("Caught an AmazonServiceException, which means your request made it "
                + "to Amazon S3, but was rejected with an error response for some reason.");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
            logger.info(ase.getMessage(), ase);
            logger.info("====================================AWS S3 UPLOAD ERROR END======================================");
            throw new AmazonClientException("error occurs during upload to s3!");
        } catch (AmazonClientException ace) {
            ace.printStackTrace();
            logger.info("====================================AWS S3 UPLOAD ERROR START======================================");
            logger.info("Caught an AmazonClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with S3, "
                + "such as not being able to access the network.");
            logger.info("Error Message: " + ace.getMessage());
            logger.info("====================================AWS S3 UPLOAD ERROR END======================================");
            throw new AmazonClientException("error occurs during upload to s3!");
        }
    }
    */
/**
     * 验证s3上是否存在名称为bucketName的Bucket
     * @param s3
     * @param bucketName
     * @return
     *//*

    public static boolean checkBucketExists (AmazonS3 s3, String bucketName) {
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket bucket : buckets) {
            if (Objects.equals(bucket.getName(), bucketName)) {
                return true;
            }
        }
        return false;
    }
    @SuppressWarnings({ "deprecation", "unused" })
    public static String getFileUrlByKey(String key) throws IOException {
        //首先创建一个s3的客户端操作对象（需要amazon提供的密钥）
        s3 = new AmazonS3Client(new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY));
        Region usWest2 = Region.getRegion(Regions.CN_NORTH_1);
        s3.setRegion(usWest2);
        try {
            S3Object s3Object = s3.getObject(new GetObjectRequest(bucket_name, key));
            //获取一个request
            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucket_name, key);
            //生成公用的url
            URL url = s3.generatePresignedUrl(urlRequest);
            System.out.println("=========URL=================" + url + "============URL=============");
            if (url == null) {
                throw new AmazonClientException("can't get s3 file url!");
            }
            return url.toString();
        } catch (AmazonServiceException ase) {
            ase.printStackTrace();
            logger.info("====================================AWS S3 UPLOAD ERROR START======================================");
            logger.info("Caught an AmazonServiceException, which means your request made it "
                + "to Amazon S3, but was rejected with an error response for some reason.");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
            logger.info(ase.getMessage(), ase);
            logger.info("====================================AWS S3 UPLOAD ERROR END======================================");
            throw new AmazonServiceException("error occurs during upload to s3!");
        } catch (AmazonClientException ace) {
            ace.printStackTrace();
            logger.info("====================================AWS S3 UPLOAD ERROR START======================================");
            logger.info("Caught an AmazonClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with S3, "
                + "such as not being able to access the network.");
            logger.info("Error Message: " + ace.getMessage());
            logger.info("====================================AWS S3 UPLOAD ERROR END======================================");
            throw new AmazonClientException("error occurs during upload to s3!");
        }
    }


    public static void main(String[] args) {
        //1
//      String key = UUID.randomUUID() + ".wangmeng";
//      logger.info("key : "+key);
//      uploadFile("/Users/zhangjiamei/Downloads/FAQ.xls", bucket_name, key, true);
        //2.
//      File file = new File("/Users/zhangjiamei/Downloads/order_4.xls");
//      try {
//          uploadToS3(file, bucket_name);
//      } catch (Exception e) {
//          // TODO: handle exception
//          e.printStackTrace();
//      }
        //3.
        File file = new File("/Users/zhangjiamei/Downloads/test1.txt");
        try {
            uploadToS3(new FileInputStream(file),UUID.randomUUID()+".pg");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}*/
