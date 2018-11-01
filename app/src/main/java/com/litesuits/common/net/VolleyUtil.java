package com.litesuits.common.net;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.yousucc.database.GlobalSharedPreferences;
import com.yousucc.database.PreferencesSetting;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Calvin on 15-10-20.
 */
public class VolleyUtil {

    public static Request doGet(String url, final RequestCallback callback) {
        return doGet(url, callback, null);
    }

    public static Request doGet(String url, final RequestCallback callback, Object tag) {
        return doRequest(Request.Method.GET, url, null, callback, tag);
    }

    public static Request doPost(String url, Map<String, String> paramMap, final RequestCallback callback) {
        return doPost(url, paramMap, callback, null);
    }

    public static Request doPost(String url, Map<String, String> paramMap, final RequestCallback callback, Object tag) {
        return doRequest(Request.Method.POST, url, paramMap, callback, tag);
    }

    public static Request doDel(String url, Map<String, String> paramMap, final RequestCallback callback, Object tag) {
        return doRequest(Request.Method.DELETE, url, paramMap, callback, tag);
    }

    public static void cancelRequest(Object tag) {
        VolleyRequest.cancelRequest(tag);
    }

    /**
     * @param method the request {@link Request.Method} to use
     */
    public static Request doRequest(int method, final String url, final Map<String, String> paramMap, final RequestCallback callback, Object tag) {
        CookieRequest request = new CookieRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (null != response) {
                    int statusCode = response.statusCode;
                    byte[] data = response.data;
                    try {
                        Map<String, String> headers = response.headers;
                        callback.onFailure(statusCode, new String(data, HttpHeaderParser.parseCharset(headers)));
                    } catch (UnsupportedEncodingException e) {
                        callback.onFailure(statusCode, new String(data));
                    }
                } else {
                    callback.onFailure(0, "未知错误,请稍后重试");
                }
            }
        }, paramMap);
        if (null == tag) {
            //tag为null,以url为tag
            tag = url;
            request.setTag(tag);
        }
        request.setRetryPolicy(new DefaultRetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }
        });
        String localCookieStr = GlobalSharedPreferences.getInstance().getStringPreferences(PreferencesSetting.SETTING_COOKIE.getSettingName(), (String) PreferencesSetting.SETTING_COOKIE.getDefaultValue());
        Log.e("发送cookie--->",localCookieStr);
        if (!localCookieStr.equals("")) {
            request.setSendCookie(localCookieStr);//向服务器发起post请求时加上cookie字段
        }
        return VolleyRequest.addRequest(request);
    }
}
