package com.httpclient.utils;

/**
 * 利率计算
 * 
 * @author onlyone
 */
public class RateTest {

    public static void main(String[] args) {

        long moneySum = 220000L;
        float rateForYear = 0.0285f;

        float lixiOneMonth = moneySum * rateForYear / 12;

        float benjinMonth = moneySum / 60;

        float rate = 0f;
        for (int i = 1; i <= 60; i++) {
            float k = lixiOneMonth / (moneySum - (i - 1) * benjinMonth);
            System.out.println("第" + i + "个月利率：" + k);
            rate += k;
            if (i % 12 == 0) {
                System.out.println("第" + (i / 12) + "年利率：" + rate);
                rate = 0;
            }
        }
    }
}
