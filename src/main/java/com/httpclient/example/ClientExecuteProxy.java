package com.httpclient.example;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 通过代理的方式发送一个请求
 * 
 * @author onlyone
 */
public class ClientExecuteProxy {

    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            // 本地启动Charles，做为代理服务器
            HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();

            HttpPost httpPost = new HttpPost("http://zhangzhaoaaa.iteye.com/blog/2094680");
            httpPost.setConfig(config);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
}
