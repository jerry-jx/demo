package com.example.demo.units;


/**
 * Created by jerry-jx on 2017/7/31.
 */
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.util.Base64Utils;

/**
 * EncryptUtils 加密、解密
 */
@Slf4j
public class EncryptUtils {

    public static final String KEY_ALGORITHM_RSA = "RSA";

    private static final String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJvC7pucMGoNg17Wp7kb0/U8QjX9UVKTlf9aGiVilylrQXzDscjjfxy28c1BhshUVP90WrzE/u2BODnne2weKVBI1UFY8F8N2eOSwZSTZBHaW5rCgYSVw3VVUQLfvp6jKKJ9kMZbPxYUN7OhOnNxQ3Qn5xVbF5NbanyD80FZaUFwIDAQAB";
    private static final String RSA_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIm8Lum5wwag2DXtanuRvT9TxCNf1RUpOV/1oaJWKXKWtBfMOxyON/HLbxzUGGyFRU/3RavMT+7YE4Oed7bB4pUEjVQVjwXw3Z45LBlJNkEdpbmsKBhJXDdVVRAt++nqMoon2Qxls/FhQ3s6E6c3FDdCfnFVsXk1tqfIPzQVlpQXAgMBAAECgYAIel7g4CBTDh+A4kZFqOjfNwbBrqptDQaNE4+JcIhIl2IqfNW+ojprVbxXJ+8A7wsfKvdM5dydtQsudNillTWfI19UfcyY3N06KuZoyy4gcZUodfGjIhj3v3AqlLn99Qg8fmZN9AqT5xtLppPQ8IYBwJFBdLzDlvWCasHwQPEdkQJBAMxrxRnqI7bQ5u+1/ng3vrWtSUck2CIJp4ZIOQBqd9B99ZAbCL5wchV6/XYMD7dXyyPVra1aNLpV7xxqD4+oMlsCQQCsfPNOU92xGJTBnnww419EdTMmDbd58Riaox0ujBJWPAkQ5rvLRowCF8TZg17e3el7P2sRKe+W2ZcFKZulcpn1AkEAk4oxDxwrvLYYB+k+CfyJ+8xfOCcCi8aWD9gus/skEOnog6LPc8vqu1AygbIA4d5OssV1fcm+hsKhOsveAVxvQwJASizlmJ56LIlMl0LnV0LAGjloSIoHh9oFYaKZjwXPcOQaZflDcvQgi1nbXkqfRqoZmNW53kSxcqaZyi+NboWs7QJBALKojgl4m8/emViv8BBbi3hhRLt6U1DiXco9BaaK7LVB5kNUVZiGs1dhBDLzRJEmHtV/V8YzvYeYCFYH3ApZrUE=";

    public static void main(String[] args) throws Exception {

        //获取公钥和私钥
        RSAPublicKey publicKey = loadPublicKeyByStr(RSA_PUBLIC_KEY);
        RSAPrivateKey privateKey = loadPrivateKeyByStr(RSA_PRIVATE_KEY);

        String base64PublicKey = new String(Base64Utils.encode(publicKey.getEncoded()));
        String base64PrivateKey = new String(Base64Utils.encode(privateKey.getEncoded()));
        System.out.println("base64PublicKey=" + base64PublicKey);
        System.out.println("base64PrivateKey=" + base64PrivateKey);

        System.out.println("base64PrivateKeyStr=" + loadPrivateKeyByStr(base64PrivateKey));

        //模
        String modulus = publicKey.getModulus().toString();
        //公钥指数
        String public_exponent = publicKey.getPublicExponent().toString();
        //私钥指数
        String private_exponent = privateKey.getPrivateExponent().toString();
        System.out.println("modulus : " + modulus + "\n");
        System.out.println("modulus 16: " + publicKey.getModulus().toString(16) + "\n");
        System.out.println("public exponent : " + public_exponent + "\n");
        System.out.println("public exponent 16: " + publicKey.getPublicExponent().toString(16) + "\n");
        System.out.println("private exponent : " + private_exponent + "\n");

        //明文
        String ming = "111111";
        //使用模和指数生成公钥和私钥
        RSAPublicKey pubKey = EncryptUtils.getPublicKey(
            modulus,
            public_exponent);
        RSAPrivateKey priKey = EncryptUtils.getPrivateKey(
            modulus,
            private_exponent);

        //加密后的密文
        String mi = EncryptUtils.encryptByPublicKey(ming, publicKey);
        //密文base64加密
        String base64Mi = Base64Utils.encodeToString(mi.getBytes());
        System.err.println("encodeMsg : " + base64Mi + "\n");
        //======================================
        //解密后的明文
        base64Mi = "ODcyMjg2NTIxQjFFMjIxM0E2OEQ2QTNFQUJFMDAyQThGQTY3OUExREE5NzQ2ODRCMDZEMkU2MTEzODMxNUM3MDI2MjQxNEM1NTBDMDlFNTExMTFGNEYxREE2QThBMTJGN0Y3RUMyNDVGMUMxNzM5RjcwMkZCNDcxM0YxRDlCQTJDNUU0RUI4N0U5NjE4OTg1NDFDNjQzM0UzNDVCODBGRDFCOUMyQjc0NTJBRDQ0NDlDOEJDNjAwNjQ0NjIwMkVEMDlDQzZFNUUzRERCMDYyMjQ4NEU3MTA5NjBGMkMxMThDQkU0M0RFOTRCMEVEOERENUQ2REIyRTA5RERGOEY1Rg";
        ming = EncryptUtils.decryptByPrivateKey(base64Mi, privateKey);
        System.err.println("decodeMsg : " + ming + "\n");
         //123456== MjdCOUVDQjNFNDQ2NUJCMDM1ODRFRDk2MjU3MzUyRTc3N0RFQTIwMDM2QTA5MDA3NzcyRDUyMkIxMTI3RERGQUU2RTJCRTE5OERDQjU5NkFFRkI2RjcwNDdFMjM5OTc1NkY4MDgxRTA5OUIwQzkxQ0FFREVERTVBRjdDMkZBQzRBQ0NGQzM4ODQ4QjgwRkNFMUE0Mjk5NUUyQTc1RjQ5MEE3QTE1MzJBNEE3Qzg2MDBDQkFFQTM3RTFDNTJEQjE2RUNCQ0U3NEYxODhEMDRGQUZDRUIyRkJGRURFNTAzQ0RCRkYyQzYzNDQ1MjlDMzBFRTkwNzZCQTA5Nzc3ODE0Mw==
         //1234  == Njg4NDkyYzBjY2MxMThjMzgwNjRiYjBmYjFmNGExZjIwYjhhMWViMjJmZmM0ZGI1NDNlZGYxMDJjMWYzYTMyZGY3NDIyNDIxNjU3MGIyZTg3NjkwYjljNjQ2MDFiNTk3ODgxNjdiYjBjNTEwMmU5ZjdmZjA0NzAzZDIyZDg0ZjY1MWZlMjZhYTdjMDJiNWM5YmRhNWI4MGJjZTY2ZjgyYjU4Mzc4OWQzZjhhOTBkMDMwMDc5MDIzZDA4NTg1YzRlNTA1YjFmNDcwZTNiMzQ1MDllZjExM2E3ODdhMGM1YmJhYTEyODNlY2IxYTI0YmNiMDk4NmRlZjA1OWJhMDg2NQ
         //1234  == 688492c0ccc118c38064bb0fb1f4a1f20b8a1eb22ffc4db543edf102c1f3a32df74224216570b2e87690b9c64601b59788167bb0c5102e9f7ff04703d22d84f651fe26aa7c02b5c9bda5b80bce66f82b583789d3f8a90d030079023d08585c4e505b1f470e3b34509ef113a787a0c5bbaa1283ecb1a24bcb0986def059ba0865
    }

    /**
     * 服务端解密方法
     */
    public static String decryptMethod(String encryptData) {
        String rsaDecryptedStr = null;
        try {
            RSAPrivateKey privateKey = loadPrivateKeyByStr(RSA_PRIVATE_KEY);
            rsaDecryptedStr = EncryptUtils.decryptByPrivateKey(encryptData, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsaDecryptedStr;
    }


    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus 模
     * @param exponent 指数
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用模和指数生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus 模
     * @param exponent 指数
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥加密
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
        throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长
        int key_len = publicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11
        String[] datas = splitString(data, key_len - 11);
        String mi = "";
        //如果明文长度大于模长-11则要分组加密
        for (String s : datas) {
            mi += bcd2Str(cipher.doFinal(s.getBytes()));
        }
        return mi;
    }

    /**
     * 私钥解密
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)
        throws Exception {
        //base64解密原始密文
        String decryptData = new String(Base64Utils.decodeFromString(data));

        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //模长
        int key_len = privateKey.getModulus().bitLength() / 8;
        byte[] bytes = decryptData.getBytes();
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
        System.err.println(bcd.length);
        //如果密文长度大于模长则要分组解密
        String ming = "";
        byte[][] arrays = splitArray(bcd, key_len);
        for (byte[] arr : arrays) {
            ming += new String(cipher.doFinal(arr));
        }
        return ming;
    }

    /**
     * ASCII码转BCD码
     */
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    public static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9')) {
            bcd = (byte) (asc - '0');
        } else if ((asc >= 'A') && (asc <= 'F')) {
            bcd = (byte) (asc - 'A' + 10);
        } else if ((asc >= 'a') && (asc <= 'f')) {
            bcd = (byte) (asc - 'a' + 10);
        } else {
            bcd = (byte) (asc - 48);
        }
        return bcd;
    }

    /**
     * BCD转字符串
     */
    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    /**
     * 拆分字符串
     */
    public static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    /**
     * 拆分数组
     */
    public static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }


    /**
     * 转换base64加密后的字符串的原始私钥
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr)
        throws Exception {
        try {
            byte[] buffer = Base64.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }


    /**
     * 转换base64加密后的字符串的原始公钥
     */
    private static RSAPublicKey loadPublicKeyByStr(String keyStr) {
        byte[] keyBytes = Base64.decode(keyStr);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        RSAPublicKey publicKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
            publicKey = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return publicKey;
    }

    public void aaaa (){
        String a ="aa";
        String b = "bb";

        if((a.isEmpty() || a == null) || (b.isEmpty() || b == null)){

        }
    }

}

