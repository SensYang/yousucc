package com.yousucc.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.yousucc.R;
import com.yousucc.database.GlobalSharedPreferences;
import com.yousucc.database.PreferencesSetting;
import com.yousucc.ui.activity.authentication.LoginActivity;
import com.yousucc.ui.base.BaseActivity;

/**
 * Created by SensYang on 2016/3/18 0018.
 */
public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!GlobalSharedPreferences.getInstance().isContainsKey(PreferencesSetting.SETTING_HASGUIDE.getSettingName())) {
                    GlobalSharedPreferences.getInstance().setBooleanPreferences(PreferencesSetting.SETTING_HASGUIDE.getSettingName(), true);
                    startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
                    finish();
                    return;
                }
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}
