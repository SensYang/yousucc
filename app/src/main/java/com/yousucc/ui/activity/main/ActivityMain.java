package com.yousucc.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.yousucc.R;
import com.yousucc.chat.IMChattingHelper;
import com.yousucc.database.GlobalSharedPreferences;
import com.yousucc.database.PreferencesSetting;
import com.yousucc.database.tables.ChatMessage;
import com.yousucc.ui.activity.authentication.LoginActivity;
import com.yousucc.ui.activity.chat.ChatActivity;
import com.yousucc.ui.adapter.MainTablePagerAdapter;
import com.yousucc.ui.base.BaseActivity;
import com.yousucc.ui.base.slidback.IntentUtils;
import com.yousucc.ui.widget.MainTableView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SensYang on 2016/3/26 0026.
 */
public class ActivityMain extends BaseActivity implements IMChattingHelper.OnMessageReportCallback {
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.mainTableView)
    MainTableView mainTableView;
    private MainTablePagerAdapter adapter;
    private ArrayList<IMChattingHelper.OnMessageReportCallback> messageCallbackList = new ArrayList<>();
    private static ActivityMain instances;

    public static ActivityMain getInstances() {
        return instances;
    }

    /**
     * 添加消息监听器
     */
    public void addMessageReportCallback(IMChattingHelper.OnMessageReportCallback onMessageReportCallback) {
        messageCallbackList.add(onMessageReportCallback);
    }

    /**
     * 删除消息监听器
     */
    public void removeMessageReportCallback(IMChattingHelper.OnMessageReportCallback onMessageReportCallback) {
        messageCallbackList.remove(onMessageReportCallback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instances = this;
        GlobalSharedPreferences.getInstance().setStringPreferences(PreferencesSetting.SETTING_CHATWITH.getSettingName(), (String) PreferencesSetting.SETTING_CHATWITH.getDefaultValue());
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if (intent != null) {
            String chatWith = intent.getStringExtra("chatwith");
            if (chatWith != null) {
                intent.setClass(this, ChatActivity.class);
                startActivity(intent);
            }
        }
        ButterKnife.bind(this);
        initListener();
        if (LoginActivity.instance != null)
            LoginActivity.instance.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentUtils.getInstance().clear();
    }

    private void initListener() {
        adapter = new MainTablePagerAdapter(this, viewPager, mainTableView);
        IMChattingHelper.setOnMessageReportCallback(this);
    }

    @Override
    public void onMessageDowloaded(ChatMessage message) {
        for (IMChattingHelper.OnMessageReportCallback callback : messageCallbackList) {
            callback.onMessageDowloaded(message);
        }
    }

    @Override
    public void onPushMessage(List<ChatMessage> chatMessageList) {
        for (IMChattingHelper.OnMessageReportCallback callback : messageCallbackList) {
            callback.onPushMessage(chatMessageList);
        }
    }
}
