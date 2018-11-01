package com.yousucc.ui.base;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class WeakReferenceHandler extends Handler {
    private WeakReference<BaseActivity> mActivity = null;
    private WeakReference<BaseFragment> mFragment = null;


    public WeakReferenceHandler() {

    }


    public WeakReferenceHandler(BaseActivity activity) {
        mActivity = new WeakReference<>(activity);
    }


    public WeakReferenceHandler(BaseFragment fragment) {
        mFragment = new WeakReference<>(fragment);
    }

    @Override
    public void handleMessage(Message msg) {
        if (null != mActivity && null != mActivity.get()) {
            mActivity.get().handlerPacketMsg(msg);
        }
        if (null != mFragment && null != mFragment.get()) {
            mFragment.get().handlerPacketMsg(msg);
        }
    }
}
