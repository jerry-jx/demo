package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by jerry-jx on 2018/5/7.
 */
public class CurlTest {


//    public static void main(String[] args)
//    {
//        HttpURLConnection connection = null;
//        try {
//            //store necessary query information
//            URL jiraURL = new URL("https://jirainstance.atlassian.net/rest/api/2/issue");
//            String data = "{\n" +
//                "    \"fields\": {\n" +
//                "       \"project\":\n" +
//                "       { \n" +
//                "          \"key\": \"TP\"\n" +
//                "       },\n" +
//                "       \"summary\": \"REST ye merry gentlemen.\",\n" +
//                "       \"description\": \"Creating of an issue using project keys and issue type names using the REST API\",\n" +
//                "       \"issuetype\": {\n" +
//                "          \"name\": \"Bug\"\n" +
//                "       }\n" +
//                "   }\n" +
//                "}";
//            byte[] dataBytes = data.getBytes("UTF-8");
//            String login = "username:password";
//            final byte[] authBytes = login.getBytes(StandardCharsets.UTF_8);
//            String encoded = Base64.getEncoder().withoutPadding().encodeToString(authBytes);
//
//
//            //establish connection and request properties
//            connection = (HttpURLConnection) jiraURL.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Accept", "*/*");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("Authorization", "Basic " + encoded);
//            connection.setUseCaches(false);
//            connection.setDoOutput(true);
//            connection.setDoInput(true);
//
//            connection.connect();
//
//            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
//            wr.write(dataBytes);
//            wr.flush();
//            wr.close();
//
//            Reader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
//            for (int c; (c = in.read()) &gt;= 0; System.out.print((char)c));
//
//        } catch (MalformedURLException e) {
//            System.err.println("MalformedURLException: " + e.getMessage());
//        } catch (IOException e) {
//            System.err.println("IOException: " + e.getMessage());
//
//            if (connection != null) {
//                try {
//                    System.out.println(connection.getResponseMessage());
//
//                    InputStream errorStream = connection.getErrorStream();
//                    if (errorStream != null) {
//                        Reader in = new BufferedReader(new InputStreamReader(errorStream));
//                        for (int c; (c = in.read()) &gt;= 0; System.out.print((char) c));
//                    }
//                } catch(IOException e2) {}
//            }
//        }
//    }


    /*public static void main(String[] args) {

        try {

            String url = "http://baidu.com";

            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            conn.setRequestMethod("PUT");

            String userpass = "user" + ":" + "pass";
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
            conn.setRequestProperty ("Authorization", basicAuth);

            String data =  "{\"format\":\"json\",\"pattern\":\"#\"}";
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(data);
            out.close();

            new InputStreamReader(conn.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/


    /*URL url = new URL("http://stackoverflow.com");

try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
        for (String line; (line = reader.readLine()) != null;) {
            System.out.println(line);
        }
    }*/

  /*  URL url = new URL("http://quora.com");

try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
        for (String line; (line = reader.readLine()) != null;) {
            System.out.println(line);
        }
    }

    public String getGoogleHomepage (){
        //-L is passed to follow the redirects
        return curl ().lUpperCase ().$ ("https://www.google.com/");
    }*/
}
