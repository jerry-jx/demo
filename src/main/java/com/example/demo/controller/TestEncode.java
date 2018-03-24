package com.example.demo.controller;

import static com.amazonaws.util.AWSRequestMetrics.Field.Exception;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSONObject;
/**
 * Created by jerry-jx on 2018/3/16.
 */
public class TestEncode {
    private static final Logger logger = LoggerFactory.getLogger(TestEncode.class);
    public static void main(String[] args) throws Exception {
        JSONObject condition = new JSONObject();
        condition.put("payAmount", "1");
        condition.put("merchantCode", "XM01");
        condition.put("transDesc", "选择支付渠道");
        condition.put("bizNo", "dadad3333");
        condition.put("bizType", "76");
        condition.put("idType", "5");
        condition.put("accountName", "卫涵菡");
        condition.put("serialNo", "2");
        condition.put("idNo", "131124197907242963");
        String encryptKey = "abc";
        String result1 = aesEncrypt1(condition.toJSONString(),encryptKey);
        String result2 = aesEncrypt2(condition.toJSONString(),encryptKey);
        logger.info(result1);
        logger.info(result2);
    }
    public static String aesEncrypt1(String content, String encryptKey) throws Exception {
        logger.info("加密前的数据content={}",content);
        return base64Encode1(aesEncryptToBytes(content, encryptKey));
    }

    public static String aesEncrypt2(String content, String encryptKey) throws Exception {
        logger.info("加密前的数据content={}",content);
        return base64Encode2(aesEncryptToBytes(content, encryptKey));
    }


    public static String base64Encode1(byte[] bytes) {
        return new String(new Base64().encode(bytes));

    }

    public static String base64Encode2(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);
    }


    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(encryptKey.getBytes());
        kgen.init(128, random);

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

        return cipher.doFinal(content.getBytes("utf-8"));
    }
}
