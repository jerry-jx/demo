/*
package com.example.demo.controller.HTMLCatch;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Scanner;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.core.weibo.tencent.api.UserAPI;
import org.core.weibo.tencent.oauthv2.OAuthV2;
import org.core.weibo.tencent.oauthv2.OAuthV2Client;

*/
/***
 * 腾迅自动登录并获取个人信息
 * @author zdw
 *
 *//*

public class Tencent
{
    public static final String HEXSTRING = "0123456789ABCDEF";
    public static OAuthV2 oAuth = new OAuthV2();
    private static HttpClient client = new DefaultHttpClient();
    // 初始oAuth应用信息
    public static void init(OAuthV2 oAuth)
    {
        oAuth.setClientId("801216331");
        oAuth.setClientSecret("ea71b26b0cbe5778cdd1c09ad17553a3");
        oAuth.setRedirectUri("http://www.tencent.com/zh-cn/index.shtml");
    }
    */
/**
     *
     * @param qq
     *      http://check.ptlogin2.qq.com/check?uin={0}&appid=15000101&r={1 }
     *      返回的第三个值
     * @param password
     *      QQ密码
     * @param verifycode
     *      验证码
     * @return 加密后的密码
     * @throws UnsupportedEncodingException
     * @throws Exception
     *
     *//*

    public static String GetPassword(String qq, String password,
        String verifycode) throws Exception
    {
        String P = hexchar2bin(md5(password));
        String U = md5(P + hexchar2bin(qq.replace("\\x", "").toUpperCase()));
        String V = md5(U + verifycode.toUpperCase());
        return V;
    }

    public static String md5(String originalText) throws Exception
    {
        byte buf[] = originalText.getBytes("ISO-8859-1");
        StringBuffer hexString = new StringBuffer();
        String result = "";
        String digit = "";
        try
        {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(buf);
            byte[] digest = algorithm.digest();
            for (int i = 0; i < digest.length; i++)
            {
                digit = Integer.toHexString(0xFF & digest[i]);
                if (digit.length() == 1)
                {
                    digit = "0" + digit;
                }
                hexString.append(digit);
            }
            result = hexString.toString();
        }
        catch (Exception ex)
        {
            result = "";
        }
        return result.toUpperCase();
    }

    public static String hexchar2bin(String md5str) throws UnsupportedEncodingException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(md5str.length() / 2);
        for (int i = 0; i < md5str.length(); i = i + 2)
        {
            baos.write((HEXSTRING.indexOf(md5str.charAt(i)) << 4 | HEXSTRING
                .indexOf(md5str.charAt(i + 1))));
        }
        return new String(baos.toByteArray(), "ISO-8859-1");
    }
    */
/***
     * 模拟登录
     * @param qq QQ号码
     * @param password QQ密码
     * @throws Exception
     *//*

    public static void login(String qq, String password) throws Exception
    {
        HttpGet get = new HttpGet("https://ssl.ptlogin2.qq.com/check?uin="+ qq + "&appid=46000101&ptlang=2052&js_type=2&js_ver=10009&r=0.7948186025712065");
        HttpResponse response = client.execute(get);
        String entity = EntityUtils.toString(response.getEntity());
        String[] checkNum = entity.substring(entity.indexOf("(") + 1,entity.lastIndexOf(")")).replace("'", "").split(",");
        String pass = "";
        String responseData = "";
        // 获取验证码(如果有验证码输出到C:/code.jpg,查看后输入可继续执行
        if ("1".equals(checkNum[0]))
        {
            // uin为qq号或者微博用户名
            HttpGet getimg = new HttpGet("http://captcha.qq.com/getimage?aid=46000101&r=0.3478789969909082&uin=" + qq + "&vc_type=" + checkNum[1] + "");
            HttpResponse response2 = client.execute(getimg);
            OutputStream os = new FileOutputStream("c:/code.jpg");
            byte[] b = EntityUtils.toByteArray(response2.getEntity());
            os.write(b, 0, b.length);
            os.close();
            Scanner in = new Scanner(System.in);
            responseData = in.nextLine();
            in.close();
        }
        else
        {
            responseData = checkNum[1];
        }
        */
/** *******************加密密码 ************************** *//*

        pass = GetPassword(checkNum[2], password, responseData);
        */
/** *********************** 登录 *************************** *//*

        HttpGet getimg = new HttpGet("https://ssl.ptlogin2.qq.com/login?ptlang=2052&u="+ qq+ "&p="+ pass+ "&verifycode="+ responseData+ "&aid=46000101&target=top&u1=https%3A%2F%2Fopen.t.qq.com%2Fcgi-bin%2Foauth2%2Fauthorize%3Fclient_id%3D"
            + oAuth.getClientId()+ "%26response_type%3Dcode%26redirect_uri="+ oAuth.getRedirectUri()+ "&ptredirect=1&h=1&from_ui=1&dumy=&qlogin_param=abbfew=ddd&wording=%E6%8E%88%E6%9D%83&fp=loginerroralert&action=8-13-240977&g=1&t=1&dummy=&js_type=2&js_ver=10009");
        HttpResponse response2 = client.execute(getimg);
        HttpEntity httpentity = response2.getEntity();
        String entityxc = EntityUtils.toString(httpentity);
        System.out.println(entityxc);
    }

    */
/**
     *
     * 请求微博开放平台应用 返回登录授权页面，但是如果没有sessionKey的话永远登录不成功 sessionKey
     * 发现在返回的页面中一个input标签里放的url中有，所以要取到这个sessionKey 其实直接访问标签中的url就可以跳转
     *
     *//*

    public static String getUrl() throws ClientProtocolException, IOException
    {
        HttpGet getcode = new HttpGet("https://open.t.qq.com/cgi-bin/oauth2/authorize?client_id="+ oAuth.getClientId()+ "&response_type=code&redirect_uri="
            + oAuth.getRedirectUri()+ "&checkStatus=yes&appfrom=&g_tk&checkType=showAuth&state=");
        HttpResponse response3 = client.execute(getcode);
        HttpEntity entityqqq = response3.getEntity();
        String entityxcc = EntityUtils.toString(entityqqq);
        String form = entityxcc.substring(entityxcc.indexOf("<form"), entityxcc
            .indexOf("</form>"));
        String[] ss = form.split("/>");
        String input = "";
        for (int i = 0; i < ss.length; i++)
        {
            if (ss[i].indexOf("name=\"u1\"") > 0)
            {
                input = ss[i];
            }
            ;
        }
        return input.substring(input.indexOf("value=\"") + 7, input.indexOf("\" type=\""));
    }
    */
/**
     * 解析并设置Token
     * @param get
     * @throws Exception
     *//*

    public static void setToken(HttpGet get) throws Exception
    {
        HttpResponse response4 = client.execute(get);
        HttpEntity entityqqq1 = response4.getEntity();
        String getUrlcode = EntityUtils.toString(entityqqq1);
        // 返回了最终跳转的页面URL，也就是回调页redirect_uri,页面地址上包含code openid openkey
        // 需要将这三个值单独取出来再拼接成 code=xxxxx&openid=xxxxx&openkey=xxxxxx的形式
        String entity = getUrlcode.substring(getUrlcode.indexOf("url="),getUrlcode.indexOf("\">"));
        StringBuffer sb = new StringBuffer();
        String[] arr = entity.split("\\?")[1].split("&");
        for (int x = 0; x < arr.length; x++)
        {
            if (arr[x].indexOf("code") >= 0 || arr[x].indexOf("openid") >= 0
                || arr[x].indexOf("openkey") >= 0)
            {
                sb.append(arr[x] + "&");
            }
            ;
        }
        // 利用code获取accessToken
        OAuthV2Client.parseAuthorization(sb.substring(0, sb.length() - 1), oAuth);
        oAuth.setGrantType("authorize_code");
        OAuthV2Client.accessToken(oAuth);
    }
    */
/***
     * 调用(腾迅开放平台账户接口)获取一个人的信息
     * @throws Exception
     *//*

    public static void getInfo() throws Exception
    {
        //输出Token，如果拿到了Token就代表登录成功，并可以进行下一步操作。
        System.out.println("Token="+oAuth.getAccessToken());
        UserAPI getuser = new UserAPI(oAuth.getOauthVersion());
        String userJson = getuser.otherInfo(oAuth, "json", "", oAuth.getOpenid());
        JSONObject userJsonObject = JSONObject.fromObject(userJson);
        Integer errcode = (Integer) userJsonObject.get("errcode");
        if (errcode == 0)
        {
            JSONObject userdataJsonObject = (JSONObject) userJsonObject.get("data");
            System.out.println(userdataJsonObject.toString());
        }
    }

    public static void main(String[] args) throws Exception
    {
        init(oAuth);
        login("123145", "xxxx");
        HttpGet get = new HttpGet(getUrl());
        setToken(get);
        getInfo();
    }



}*/
