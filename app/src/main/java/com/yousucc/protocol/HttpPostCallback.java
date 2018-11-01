package com.yousucc.protocol;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.litesuits.common.net.ParseJson2BeanUtils;
import com.litesuits.common.net.RequestCallback;

public class HttpPostCallback<T> implements RequestCallback {
    private Class<T> beanClass;
    private boolean isRunOnUiThread = false;
    private Handler uiHandler;

    public HttpPostCallback(boolean isRunOnUiThread, Class<T> beanClass) {
        this.isRunOnUiThread = isRunOnUiThread;
        this.beanClass = beanClass;
    }

    /**
     * 数据返回成功
     *
     * @param result 已解析的返回内容
     */
    public void resultSuccess(T result) {
    }

    /**
     * 数据获取失败
     */
    public void resultFailure() {
    }

    @Override
    public final void onSuccess(final String response) {
        Log.e("HttpPostCallback--->", response.toString());
        if (isRunOnUiThread) {
            if (uiHandler == null) uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
//                    if (beanClass != String.class)
                        resultSuccess(ParseJson2BeanUtils.<T>parseJson2Bean(response, beanClass));
//                    else resultSuccess((T) response);
                }
            });
        } else {
//            if (beanClass != String.class)
                resultSuccess(ParseJson2BeanUtils.<T>parseJson2Bean(response, beanClass));
//            else resultSuccess((T) response);
        }
    }

    @Override
    public final void onFailure(int statusCode, String responseString) {
        Log.e("HttpPostCallback--->", statusCode + "----" + responseString);
        resultFailure();
    }
}
