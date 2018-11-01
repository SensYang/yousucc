package com.yousucc.config;

public class ConfigNet {
    public static final boolean ISONLINE = Config.ISONLINE;
    public static final String HTTP_DELETE = "DELETE";
    public static final String HTTP_POST = "POST";
    public static final String HTTP_GET = "GET";
    //服务器地址及找回密码配置地址（开发环境）
    private static final String HTTP_SERVER_HOST_DISLINE = "http://120.25.155.22:8080/yousucc";
    //服务器地址及找回密码地址（线上环境）
    private static final String HTTP_SERVER_HOST_ONLINE = "http://www.yousucc.com:8080/yousucc";          //线上短链接
    public static final String HTTP_SERVER_HOST = ISONLINE ? HTTP_SERVER_HOST_ONLINE : HTTP_SERVER_HOST_DISLINE;

    /**
     * 注册获取验证码
     */
    public static final String REGISTER_GET_AUTHCODE_URL = "/v0.1/register/phone";
    /**
     * 注册设置密码
     */
    public static final String REGISTER_SET_PASSWORLD_URL = "/v0.1/register/authcode";
    /**
     * 找回密码获取验证码
     */
    public static final String FIND_GET_AUTHCODE_URL = "/v0.1/retrieve/phone";
    /**
     * 找回密码设置密码
     */
    public static final String FIND_SET_PASSWORLD_URL = "/v0.1/retrieve/authcode";
    /**
     * 登录
     */
    public static final String LOGIN_URL = "/v0.1/login";

    ////////////////////////////第三方配置///////////////////////////
    /**
     * 云通讯apptoken
     */
    public static final String YUNTX_APPTOKEN = "58cb48ab1f2c07221857bf2fe553da6e";
    /**
     * 云通讯appkey
     */
    public static final String YUNTX_APPKEY = "8a48b5515388ec150153a686d12a2e08";
}
