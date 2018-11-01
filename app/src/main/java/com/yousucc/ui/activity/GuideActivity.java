package com.yousucc.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.yousucc.R;
import com.yousucc.ui.activity.authentication.LoginActivity;
import com.yousucc.ui.base.BaseActivity;

/**
 * Created by SensYang on 2016/3/31 0031.
 */
public class GuideActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}
