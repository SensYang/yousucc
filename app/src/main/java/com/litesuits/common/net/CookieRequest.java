package com.litesuits.common.net;

import android.webkit.CookieManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.yousucc.database.GlobalSharedPreferences;
import com.yousucc.database.PreferencesSetting;
import com.yousucc.utils.Log;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SensYang on 2016/3/31 0031.
 */
public class CookieRequest extends StringRequest {
    private Map<String, String> params;
    private Response.Listener<String> mListener;
    private Map<String, String> sendHeader = new HashMap<>(1);

    public CookieRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Map<String,String> params) {
        super(method, url, listener, errorListener);
        this.mListener = listener;
        this.params = params;
    }

    //当http请求是post时，则需要该使用该函数设置往里面添加的键值对
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (null == params) {
            return super.getParams();
        }
        return params;
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return sendHeader;
    }

    public void setSendCookie(String cookie) {
        sendHeader.put("Cookie", cookie);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            Map<String, String> responseHeaders = response.headers;
            String rawCookies = responseHeaders.get("Set-Cookie");
            if (rawCookies != null) {
                Log.e("CookieRequest--->", "rawCookies==== " + rawCookies);
                int end = rawCookies.indexOf(';');
                if (end != -1)
                    rawCookies = rawCookies.substring(0, end);
                Log.e("CookieRequest--->", "rawCookies==newcookie== " + rawCookies);
                if (rawCookies != null && rawCookies.length() > 0) {
                    CookieManager.getInstance().setCookie(getUrl(), rawCookies);
                    GlobalSharedPreferences.getInstance().setStringPreferences(PreferencesSetting.SETTING_COOKIE.getSettingName(), rawCookies);
                }
            }
            return Response.success(new String(response.data, "UTF-8"), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return super.parseNetworkResponse(response);
    }
}