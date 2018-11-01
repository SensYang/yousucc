package com.yousucc.utils;


import com.litesuits.common.net.RequestCallback;
import com.litesuits.common.net.VolleyUtil;
import com.yousucc.config.ConfigNet;

import java.util.Map;

/**
 * Created by cxd on 2015/2/9.
 */
public class HttpUtil {

    /**
     * 异步请求网络数据
     */
    public static void doAsynRequest(String url,  Map<String ,String> paramMap, String request_method, RequestCallback callback) {
        try {
            switch (request_method) {
                case ConfigNet.HTTP_GET:
                    VolleyUtil.doGet(url, callback);
                    break;
                case ConfigNet.HTTP_POST:
                     VolleyUtil.doPost(url, paramMap, callback);
                    break;
                case ConfigNet.HTTP_DELETE:
                    VolleyUtil.doDel(url, paramMap, callback,null);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
