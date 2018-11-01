package com.yousucc.ui.base;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yousucc.YouSuccApplication;
import com.yousucc.ui.base.slidback.IntentUtils;
import com.yousucc.ui.base.slidback.SlidingActivity;

import butterknife.ButterKnife;

/**
 *
 */
public class BaseActivity extends FragmentActivity {

    private WeakReferenceHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new WeakReferenceHandler(this);
    }

    public WeakReferenceHandler getHandler() {
        return this.handler;
    }

    protected void handlerPacketMsg(Message msg) {
    }

    @Override
    protected void onStart() {
        setTypeFace((ViewGroup) getWindow().getDecorView());
        super.onStart();
    }

    private void setTypeFace(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                setTypeFace((ViewGroup) view);
            } else if (view instanceof TextView) {
                ((TextView) view).setTypeface(YouSuccApplication.getInstance().getTypeFace());
            }
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (intent != null) {
            ComponentName componentName = intent.getComponent();
            if (componentName != null) {
                String name = componentName.getClassName();
                try {
                    if (name != null) {
                        Class cls = Class.forName(name);
                        if (cls != null) {
                            try {
                                Object instance = cls.newInstance();
                                if (instance != null) {
                                    if (instance instanceof SlidingActivity) {
                                        IntentUtils.getInstance().initNextSlid(this, intent);
                                    }
                                }
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        super.startActivityForResult(intent, requestCode, options);
    }
}
