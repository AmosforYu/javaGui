package com.yyb.gui.utils;

/**
 * @author Yamos
 * @description AppkeyEnum
 * @date 2021/1/14 0014 16:47
 */
public enum AppkeyEnum {

    DEMO("demoAppkey", "pbgwerb3y33bfcoxz3fimcz3liusg3ix"),
    TEST("testAppkey", "testAppSecrettestAppSecret"),
    ONLINE("onlineAppkey", "onlineAppSecretonlineAppSecret");

    private String appkey;
    private String appSecret;

    AppkeyEnum(String appkey, String appSecret) {
        this.appkey = appkey;
        this.appSecret = appSecret;
    }

    public static String getSecret(String appkey) {
        if (appkey.equals(DEMO.appkey)) {
            return DEMO.appSecret;
        }

        if (appkey.equals(TEST.appkey)) {
            return TEST.appSecret;
        }

        if (appkey.equals(ONLINE.appkey)) {
            return ONLINE.appSecret;
        }

        return "";
    }
}