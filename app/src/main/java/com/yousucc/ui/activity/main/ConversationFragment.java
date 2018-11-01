package com.yousucc.ui.activity.main;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.yousucc.R;
import com.yousucc.chat.IMChattingHelper;
import com.yousucc.database.GlobalSharedPreferences;
import com.yousucc.database.PreferencesSetting;
import com.yousucc.database.tables.Conversation;
import com.yousucc.database.LiteOrmDBUtil;
import com.yousucc.database.tables.ChatMessage;
import com.yousucc.ui.adapter.ConversationAdapter;
import com.yousucc.ui.base.BaseFragment;
import com.yousucc.ui.widget.TopNavigationBar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SensYang on 2016/3/27 0027.
 */
public class ConversationFragment extends BaseFragment implements IMChattingHelper.OnMessageReportCallback {
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.topNavigationBar)
    TopNavigationBar topNavigationBar;
    private ConversationAdapter conversationAdapter;
    private View parentView;

    @Override
    public void onFirstUserVisible() {
        ActivityMain.getInstances().addMessageReportCallback(this);
        conversationAdapter = new ConversationAdapter(listView);
        listView.setAdapter(conversationAdapter);
        List<Conversation> conversationList = LiteOrmDBUtil.getInstance().getQueryAll(Conversation.class);
        conversationAdapter.addConversations(conversationList);
        conversationAdapter.notifyDataSetChanged();
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        if (parentView == null) {
            parentView = inflater.inflate(R.layout.fragment_main_conversation, null, false);
            ButterKnife.bind(this, parentView);
        }
        return parentView;
    }

    @Override
    public void onMessageDowloaded(ChatMessage message) {
        if (!message.getSessionId().equalsIgnoreCase(GlobalSharedPreferences.getInstance().getStringPreferences(PreferencesSetting.SETTING_CHATWITH.getSettingName(), (String) PreferencesSetting.SETTING_CHATWITH.getDefaultValue()))) {
            //TODO

        }
    }

    /**
     * 收到信消息
     */
    @Override
    public void onPushMessage(List<ChatMessage> chatMessageList) {
        if (!chatMessageList.get(0).getSessionId().equalsIgnoreCase(GlobalSharedPreferences.getInstance().getStringPreferences(PreferencesSetting.SETTING_CHATWITH.getSettingName(), (String) PreferencesSetting.SETTING_CHATWITH.getDefaultValue()))) {
            conversationAdapter.updateConversation(chatMessageList);
        }
    }

}
