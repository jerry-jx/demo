package com.example.demo.controller.curl;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * Created by jerry-jx on 2018/5/8.
 */
public class curlTest {

    public static void main(String[] args) {
       /* String []cmds = {"curl", "-i", "-w", "状态%{http_code}；DNS时间%{time_namelookup}；"
            + "等待时间%{time_pretransfer}TCP 连接%{time_connect}；发出请求%{time_starttransfer}；"
            + "总时间%{time_total}","http://www.baidu.com"};*/
        String []cmds = {"curl", "-X", "GET", "'Accept: ","application/json'","--header","'gamePlatform:","IMONE'","'http://localhost:9009/api/v1/dsf/center/admin/customMade?access_token=ec6856fa-db28-434e-b0ea-f3a4869c182a'"};
        ProcessBuilder pb=new ProcessBuilder(cmds);
        pb.redirectErrorStream(true);
        Process p;
        try {
            p = pb.start();
            BufferedReader br=null;
            String line=null;

            br=new BufferedReader(new InputStreamReader(p.getInputStream()));
            while((line=br.readLine())!=null){
                System.out.println("\t"+line);
            }
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//curl -X GET --header 'Accept: application/json' --header 'gamePlatform: IMONE' 'http://localhost:9009/api/v1/dsf/center/admin/customMade?access_token=ec6856fa-db28-434e-b0ea-f3a4869c182a'
        //String url = "\"http://192.168.3.100:9200/_snapshot/back_2/back_133\" -d \"{\"\"\"indices\"\"\": \"\"\"indexdemo1\"\"\"}\"";
        String url = "--header 'gamePlatform: IMONE' 'http://localhost:9009/api/v1/dsf/center/admin/customMade?access_token=ec6856fa-db28-434e-b0ea-f3a4869c182a'";
        curl("-X", url);
    }

//curl -XPUT http://192.168.3.100:9200/_snapshot/back_2/back_6 -d "{\"indices\": \"indexdemo1\"}"


    public static void curl(String type, String url) {
        String[] cmds = {"curl", type, url};
        ProcessBuilder pb = new ProcessBuilder(cmds);
        pb.redirectErrorStream(true);
        Process p;
        try {
            p = pb.start();
            BufferedReader br = null;
            String line = null;
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = br.readLine()) != null) {
                System.out.println("\t" + line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
