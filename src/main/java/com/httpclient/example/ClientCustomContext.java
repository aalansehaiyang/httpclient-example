package com.httpclient.example;

import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

/**
 * 使用本地context（比如手动配置cookie）发请求
 */
public class ClientCustomContext {

    public final static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 创建本地cookie store
            CookieStore cookieStore = new BasicCookieStore();
            BasicClientCookie cookie1 = new BasicClientCookie("token", "12sfs12424234whk4h2kh234hk23");
            cookie1.setDomain("httpbin.org");
            // cookie1.setExpiryDate(cookie.getExpiry());
            // cookie1.setPath(cookie.getPath());
            cookieStore.addCookie(cookie1);

            // 创建本地HTTP 上下文
            HttpClientContext localContext = HttpClientContext.create();
            localContext.setCookieStore(cookieStore);

            // 配置代理，抓包可以查看请求cookie信息
            HttpHost proxy = new HttpHost("localhost", 8888);
            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
            HttpGet httpget = new HttpGet("http://httpbin.org/cookies");
            httpget.setConfig(config);

            // 通过本地上下文参数来发起request请求
            CloseableHttpResponse response = httpclient.execute(httpget, localContext);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                List<Cookie> cookies = cookieStore.getCookies();
                for (int i = 0; i < cookies.size(); i++) {
                    System.out.println("Local cookie: " + cookies.get(i));
                }
                EntityUtils.consume(response.getEntity());
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

}
