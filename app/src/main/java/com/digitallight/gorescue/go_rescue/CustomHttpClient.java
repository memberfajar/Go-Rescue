package com.digitallight.gorescue.go_rescue;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by F on 12/5/2015.
 */
public class CustomHttpClient {
    public static final int HTTP_TIMOUT=30*1000;
    private static HttpClient mHttpClient;
    private static HttpClient getHttpClient(){
        if (mHttpClient==null) {
            mHttpClient=new DefaultHttpClient();
            final HttpParams param=mHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(param, HTTP_TIMOUT);
            HttpConnectionParams.setSoTimeout(param, HTTP_TIMOUT);
            ConnManagerParams.setTimeout(param, HTTP_TIMOUT);

        }
        return mHttpClient;
    }
    public static String executeHttpPost(String url,ArrayList<NameValuePair> postParameters) throws Exception{
        BufferedReader in=null;
        try {
            HttpClient client=getHttpClient();
            HttpPost request=new HttpPost(url);
            UrlEncodedFormEntity formEntity=new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            HttpResponse response=client.execute(request);
            in=new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb=new StringBuffer("");
            String line="";
            String NL=System.getProperty("line.separator");
            while ((line=in.readLine())!=null) {
                sb.append(line+NL);
            }
            in.close();
            String result=sb.toString();
            return result;

        }finally{
            if (in!=null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public static String executeHttpGet(String url) throws Exception{
        BufferedReader in=null;
        try{
            HttpClient client=getHttpClient();
            HttpGet request=new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response=client.execute(request);
            in=new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb=new StringBuffer("");
            String line="";
            String NL=System.getProperty("line.separator");
            while ((line=in.readLine())!=null) {
                sb.append(line+NL);
            }
            in.close();
            String result=sb.toString();
            return result;
        }finally{
            if (in!=null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
