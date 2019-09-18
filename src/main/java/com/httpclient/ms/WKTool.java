package com.httpclient.ms;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.httpclient.utils.HttpClient4Utils;

public class WKTool {

    private static final Logger     log        = LoggerFactory.getLogger(WKTool.class);

    private static String           _cookie    = "Hm_lpvt_7f8a3fed4e5181142e4c1f289ba3dd80=1516528708; Hm_lvt_7f8a3fed4e5181142e4c1f289ba3dd80=1513954154,1514000835,1516525563; .sc_AuthToken=6EE54E77B03DCF820C18BCC7773373C5E256868828DBAD89F729055DE52749D0402A77B398AE3750B33FE6A7D90C89442DC136FFB48BB07AA70A346DFCB0F38603AEA711282A2E405FC656A9006B88B3FE000B7E44D044DCFE6DD4B5D7E21AD96E20806031F4AECF33EF5A3D116D600931D93238EF680C6E75DF994C3C970B4B722396B97E181DA718E91C0FB8875417BA23C2F1C4BBB2BC6C02D98C9686CB9A2E9A3ED5EFC9992C4D326007B561BE4509D73645560B021BBAE9FCA8B4A79835E7E9EA3F8485C8F6DFDDA733CA366D2A304377EB; fang_vanke_yevs=; sitename_vanke.efang168_uprice=%e6%9d%ad%e5%b7%9e; vanke.efang168_uprice=9; remind13488=13488; __RequestVerificationToken=bMvesQaRCJGi_wz5xe74cxFDC8kA_1gQ0GyIXQzRuC4NwRaTgDaL7XP2PQQ3OEw_jGYHsPsMS2kH9BRaALNN8VudmoM1";

    private static String           cookie     = "";

    private static HttpClient       httpClient = HttpClient4Utils.createHttpClient(10, 10, 4000, 4000, 4000);

    private static ExecutorService  pool       = new ThreadPoolExecutor(8, 8, 10000, TimeUnit.SECONDS,
                                                                        new ArrayBlockingQueue<Runnable>(1000));

    // 是否启动无限循环模式
    private static volatile boolean open       = true;

    // 意向房源
    // 基本信息

    static {
        cookie = StringUtils.replace(_cookie, "; ", ";");
    }

    public static void main(String[] args) {

        Future<String> ff = null;
        // 任务1
        String url2 = "http://mfang.vanke.com/ActivityTarget/Auction?id=2980742";
        Map<String, String> head1 = new HashMap<>();
        head1.put("Cookie", cookie);
        Map<String, String> param1 = new HashMap<>();
        ff = pool.submit(new Task(true, url2, head1, param1));

        // 任务2
        try {
            if (StringUtils.equals("continue", ff.get())) {
                url2 = "http://mfang.vanke.com/ActivityTarget/Auction?id=2980738";
                ff = pool.submit(new Task(true, url2, head1, param1));
            }
        } catch (Exception e) {
        }

        // 任务3 504
        // try {
        // if (StringUtils.equals("continue", ff.get())) {
        // url2 = "http://mfang.vanke.com/ActivityTarget/Auction?id=2869732";
        // ff = pool.submit(new Task(true, url2, head1, param1));
        // }
        // } catch (Exception e) {
        // }

        // ======================================================================================

        // 任务2
        // String url1 = "http://mfang.vanke.com/ActivityTarget/AddPrice";
        // Map<String, String> head2 = new HashMap<>();
        // head2.put("Cookie", cookie);
        // Map<String, String> param2 = new HashMap<>();
        // param2.put("id", "2867750");
        // param2.put("site", "9");
        // param2.put("price", "2353380.00");
        // param2.put("__RequestVerificationToken",
        // "y_bVcjf-1sVoL6Z4cTcUUyB2ppOoLAkZHHh6vmtEakicdSwrD1kN3pPx7M5RYnEtlue8r28I68QXoq74cmu_x1oN3HVttV2xEJXdasUC7P_3dz-K0");
        // pool.submit(new Task(false, url1, head2, param2));

        // {"Message":{"text":"抱歉,已成交,请刷新页面查看成交状态，谢谢","layout":"bottom","type":"error"},"Success":false,"DeleteId":0}

        // ======================================================================================

        // 任务3
        // Map<String, String> head3 = new HashMap<>();
        // head3.put("Cookie", cookie);
        // Map<String, String> param3 = new HashMap<>();
        // pool.submit(new Task(true, url1, head3, param3));

    }

    private static class Task implements Callable<String> {

        private boolean             isGet;
        private String              url;
        private Map<String, String> head;
        private Map<String, String> param;

        public Task(boolean isGet, String url, Map<String, String> head, Map<String, String> param){
            this.isGet = isGet;
            this.url = url;
            this.head = head;
            this.param = param;
        }

        public String call() {

            do {

                try {
                    // 刷新活动页，获取初始数据
                    long t1 = System.currentTimeMillis();
                    String response = "";
                    if (isGet) {
                        response = HttpClient4Utils.sendGet(httpClient, url, head, Consts.UTF_8);
                    } else {
                        response = HttpClient4Utils.sendPost(httpClient, url, head, param, Consts.UTF_8);
                    }
                    long t2 = System.currentTimeMillis();
                    log.info("============" + "耗时：" + (t2 - t1));
                    log.info(response);

                    String mUrl = "http://mfang.vanke.com/ActivityTarget/AddPrice";
                    Document doc = Jsoup.parse(response);
                    Map<String, String> param2 = new HashMap<>();
                    param2.put("id", doc.select("input#TargetID").get(0).attr("value"));
                    param2.put("site", "9");
                    param2.put("price", doc.select("input#currentprice").get(0).attr("value"));
                    param2.put("__RequestVerificationToken",
                               doc.select("[name=__RequestVerificationToken]").get(0).attr("value"));
                    log.info(JSON.toJSONString(param2));

                    Elements ee = doc.select(".quick_price_success");
                    if (ee != null && ee.toString().contains("选房成功")) {
                        log.info(JSON.toJSONString("============----选房成功！！！！"));
                        return "success";
                    }

                    // 抢购活动开始
                    String time = doc.select("input#countdown").get(0).attr("value");
                    Long timeLong = convertLong(time);
                    // mock
                    // SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // Date date = sd.parse("2018-01-20 23:47:00");
                    // timeLong = date.getTime() / 1000 - System.currentTimeMillis() / 1000;
                    if (timeLong > 8) {
                        log.info("*********大于8***********倒计时： " + timeLong);
                        try {
                            Thread.sleep(5000);
                        } catch (Exception e) {
                        }
                    } else if (timeLong > 1) {
                        log.info("*********大于1***********倒计时： " + timeLong);
                        try {
                            Thread.sleep((timeLong - 1) * 1000 + 500);
                        } catch (Exception e) {
                        }
                    } else if (timeLong == 1) {
                        log.info("*********等于1***********倒计时！ ");
                        // 休眠
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                        }

                    } else if (timeLong <= 0) {
                        log.info("***********秒杀开始****************秒杀开始*********，timeLong=" + timeLong);
                        String response1 = null;
                        response1 = HttpClient4Utils.sendPost(httpClient, mUrl, head, param2, Consts.UTF_8);

                        long t3 = System.currentTimeMillis();
                        log.info("============" + "耗时：" + (t3 - t2));
                        log.info(response1);

                        // 休眠
                        try {
                            Thread.sleep(300);
                        } catch (Exception e) {
                        }

                        if (response1.contains("已成交")) {
                            return "continue";
                        }
                    }

                } catch (Exception e1) {
                    log.error("e1===", e1);
                }

            } while (open);

            return "continue";
        }
    }

    private static long convertLong(String time) {
        try {
            return Long.parseLong(time);
        } catch (Exception e) {
        }
        return 0;
    }
}
