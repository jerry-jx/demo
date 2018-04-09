/*
package com.example.demo.controller.HTMLCatch.sinaLogin;

import com.sun.deploy.net.HttpUtils;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

*/
/**
 * Created by jerry-jx on 2018/4/5.
 *//*

public class SinaLogin {

    */
/**
     * @author LongJin
     * @description 初始登录信息<br> 返回false说明初始失败
     * @return
     *//*

    public boolean preLogin(){
        boolean flag = false;
        try {
            su = new String(Base64.encodeBase64(URLEncoder.encode(username, "UTF-8").getBytes()));
            String url = "http://login.sina.com.cn/sso/prelogin.php?entry=weibo&rsakt=mod&checkpin=1&" +
                "client=ssologin.js(v1.4.5)&_=" + getTimestamp();
            url += "&su=" + su;
            String content;
            content = HttpUtils.getRequest(client, url);
            System.out.println("content------------" + content);
            JSONObject json = JSONObject.fromObject(content);
            System.out.println(json);
            servertime = json.getLong("servertime");
            nonce = json.getString("nonce");
            rsakv = json.getString("rsakv");
            pubkey = json.getString("pubkey");
            flag = encodePwd();
        } catch (UnsupportedEncodingException e) {
            System.out.println("抛出UnsupportedEncoding异常");
        } catch (ClientProtocolException e) {
            System.out.println("抛出ClientProtocol异常");
        } catch (IOException e) {
            System.out.println("抛出IO异常");
        }
        return flag;
    }

    */
/**
     * @author LongJin
     * @description 登录
     * @return true:登录成功
     *//*

    public boolean login() {
        if(preLogin()) {
            String url = "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.15)";
            List<NameValuePair> parms = new ArrayList<NameValuePair>();
            parms.add(new BasicNameValuePair("entry", "weibo"));
            parms.add(new BasicNameValuePair("geteway", "1"));
            parms.add(new BasicNameValuePair("from", ""));
            parms.add(new BasicNameValuePair("savestate", "7"));
            parms.add(new BasicNameValuePair("useticket", "1"));
            parms.add(new BasicNameValuePair("pagerefer", "http://login.sina.com.cn/sso/logout.php?entry=miniblog&r=http%3A%2F%2Fweibo.com%2Flogout.php%3Fbackurl%3D%2F"));
            parms.add(new BasicNameValuePair("vsnf", "1"));
            parms.add(new BasicNameValuePair("su", su));
            parms.add(new BasicNameValuePair("service", "miniblog"));
            parms.add(new BasicNameValuePair("servertime", servertime + ""));
            parms.add(new BasicNameValuePair("nonce", nonce));
            parms.add(new BasicNameValuePair("pwencode", "rsa2"));
            parms.add(new BasicNameValuePair("rsakv", rsakv));
            parms.add(new BasicNameValuePair("sp", sp));
            parms.add(new BasicNameValuePair("encoding", "UTF-8"));
            parms.add(new BasicNameValuePair("prelt", "182"));
            parms.add(new BasicNameValuePair("url", "http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack"));
            parms.add(new BasicNameValuePair("domain", "sina.com.cn"));
            parms.add(new BasicNameValuePair("returntype", "META"));
            try {
                String content = HttpUtils.postRequest(client, url, parms);
                System.out.println("content----------" + content);
                String regex = "location.replace\\('([\\s\\S]*?)'\\);";//\\(' '\\)特殊符转译 匹配（''）里面的内容//location.replace([\\s\\S]*?)
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(content);
                if(m.find()) {
                    System.out.println("ss = "+m.group());
                    location = m.group(1);
                    if(location.contains("reason=")) {//如果你走进了这一步，恭喜报错了
                        errInfo = location.substring(location.indexOf("reason=") + 7);
                        errInfo = URLDecoder.decode(errInfo, "GBK");
                    } else {
                        System.out.println("location = "+location);
                        String result = HttpUtils.getRequest(client, location);//.substring(2, location.length()-2)
                        int beginIndex = result.indexOf("(");
                        int endIndex = result.lastIndexOf(")");
                        result = result.substring(beginIndex+1, endIndex);//截取括号里面的json字符串
                        //content = URLDecoder.decode(content, "UTF-8");
                        JSONObject jsonObject = JSONObject.fromObject(result);//转换为json
                        //获取uniqueid+userdomain用于访问时带的参数
                        uniqueid = jsonObject.getJSONObject("userinfo").getString("uniqueid");
                        userdomain = jsonObject.getJSONObject("userinfo").getString("userdomain");
                        System.out.println("result--------------" + result);
                        return true;
                    }
                }
            }  catch (ClientProtocolException e) {
                System.out.println("抛出ClientProtocol异常");
            } catch (IOException e) {
                System.out.println("抛出IO异常");
            }
        }
        return false;
    }


    */
/**
     * 密码进行RSA加密<br>
     * 返回false说明加密失败
     * @return
     *//*

    private boolean encodePwd() {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName("javascript");
        try {
            // 使用js加密密码,RSA,调用js内方法 我这里使用的是字符串 也可以直接放入文件中然后读取，如下面注释部分。
            se.eval(sina_js);
            //调用js内部函数用于加密
            if (se instanceof Invocable) {
                Invocable iv = (Invocable) se;
                sp = (String) iv.invokeFunction("getpass", this.password, this.servertime, this.nonce,
                    this.pubkey);
            }
           */
/* FileReader fr = new FileReader("E:\\encoder.js");
            se.eval(fr);
            Invocable invocableEngine = (Invocable) se;
            String callbackvalue = (String) invocableEngine.invokeFunction("encodePwd", pubkey, servertime, nonce, password);
            sp = callbackvalue;*//*

            return true;
        } catch (ScriptException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        errInfo = "密码加密失败!";
        return false;
    }


    public static void main(String[] args) throws ClientProtocolException, IOException {
        SinaWeibo weibo = new SinaWeibo("**", "***");//账号密码在此就不透露了
        if(weibo.login()) {
            System.out.println("登陆成功！");
            InputStream con= HttpUtils.getRequests(client, "http://weibo.com/u/"+uniqueid+userdomain);//请求个人主页获取输入流
            String cont = readStreamByEncoding(con, "UTF-8");//将返回的输入流转换为字符串
            String sb =HttpUtils.getText(cont);//通过jsoup获取text内容部分
            //readStreamOutFileByEncoding(sb);也可已将获取的内容写入文件中
        } else {
            System.out.println("登录失败！");
        }

    }

}
*/
