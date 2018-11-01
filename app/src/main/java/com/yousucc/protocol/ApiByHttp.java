package com.yousucc.protocol;

import com.litesuits.common.net.VolleyRequest;
import com.yousucc.YouSuccApplication;
import com.yousucc.config.ConfigNet;
import com.yousucc.utils.HttpUtil;
import com.yousucc.utils.TelephoneUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SensYang on 2016/3/25 0025.
 */
public class ApiByHttp {
    private static String sessionId;
    private static String password;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private static ApiByHttp instance;

    private ApiByHttp() {
        VolleyRequest.init(YouSuccApplication.getInstance());
    }

    public static ApiByHttp getInstance() {
        if (instance == null) {
            instance = new ApiByHttp();
        }
        return instance;
    }

    public void test( HttpPostCallback httpPostCallback) {
        String url = ConfigNet.HTTP_SERVER_HOST + "/v0.1/register/test";
        HttpUtil.doAsynRequest(url, null, ConfigNet.HTTP_GET, httpPostCallback);
    }
    /**
     * 登陆
     *
     * @param superid  用户id或手机号
     * @param password 密码
     */
    public void login(String superid, String password, HttpPostCallback httpPostCallback) {
        String url = ConfigNet.HTTP_SERVER_HOST + ConfigNet.LOGIN_URL;
        setSessionId(superid);
        setPassword(password);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("superid", superid);
        paramMap.put("password", password);
        paramMap.put("phoneid", TelephoneUtil.getIMEI(YouSuccApplication.getInstance()));
        paramMap.put("os", "android");
        HttpUtil.doAsynRequest(url, paramMap, ConfigNet.HTTP_POST, httpPostCallback);
    }

    /**
     * 获取注册的验证码
     *
     * @param phone 手机号
     */
    public void registerGetAuthcode(String phone, HttpPostCallback httpPostCallback) {
        String url = ConfigNet.HTTP_SERVER_HOST + ConfigNet.REGISTER_GET_AUTHCODE_URL;
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("phone", phone);
        HttpUtil.doAsynRequest(url, paramMap, ConfigNet.HTTP_POST, httpPostCallback);
    }

    /**
     * 注册设置密码
     *
     * @param authcode 验证码
     * @param password 密码
     */
    public void registerSetPassworld(String authcode, String password, HttpPostCallback httpPostCallback) {
        String url = ConfigNet.HTTP_SERVER_HOST + ConfigNet.REGISTER_SET_PASSWORLD_URL;
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("authcode", authcode);
        paramMap.put("password", password);
        HttpUtil.doAsynRequest(url, paramMap, ConfigNet.HTTP_POST, httpPostCallback);
    }

    /**
     * 获取找回密码的验证码
     *
     * @param superid 用户账号
     */
    public void findGetAuthcode(String superid, HttpPostCallback httpPostCallback) {
        String url = ConfigNet.HTTP_SERVER_HOST + ConfigNet.FIND_GET_AUTHCODE_URL;
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("superid", superid);
        HttpUtil.doAsynRequest(url, paramMap, ConfigNet.HTTP_POST, httpPostCallback);
    }

    /**
     * 注册设置密码
     *
     * @param authcode 验证码
     * @param password 密码
     */
    public void findSetPassworld(String authcode, String password, HttpPostCallback httpPostCallback) {
        String url = ConfigNet.HTTP_SERVER_HOST + ConfigNet.FIND_SET_PASSWORLD_URL;
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("authcode", authcode);
        paramMap.put("password", password);
        HttpUtil.doAsynRequest(url, paramMap, ConfigNet.HTTP_POST, httpPostCallback);
    }

}
