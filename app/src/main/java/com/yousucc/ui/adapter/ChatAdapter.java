package com.yousucc.ui.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.yousucc.database.tables.ChatMessage;
import com.yousucc.ui.widget.ChatItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SensYang on 2016/4/7 0007.
 */
public class ChatAdapter extends BaseAdapter {
    private AbsListView absListView;
    private List<ChatMessage> chatList = new ArrayList<>(20);
    private String type;

    public ChatAdapter(String type) {
        this.type = type;
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public ChatMessage getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (parent instanceof AbsListView) {
            absListView = (AbsListView) parent;
        }
        ChatItemView chatItemView;
        if (convertView == null) {
            chatItemView = new ChatItemView(parent.getContext());
        } else {
            chatItemView = (ChatItemView) convertView;
        }
        chatItemView.initData(chatList.get(position), type);
        return chatItemView;
    }

    private int lastCount = 0;

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.e("notifyDataSetChanged--->", (absListView == null ? "null" : absListView.getLastVisiblePosition()) + "___" + lastCount + "___" + getCount());
        if (absListView != null && absListView.getLastVisiblePosition() > lastCount - 10) {
            absListView.setSelection(getCount() - 1);
        }
    }

    public void addChatList(List<ChatMessage> chatList) {
        lastCount = getCount();
        List<ChatMessage> removeMessageList = new ArrayList<>(1);
        for (ChatMessage chatMessage : chatList) {
            for (ChatMessage hasMessage : this.chatList) {
                if(chatMessage.getMessageId().equalsIgnoreCase(hasMessage.getMessageId())){
                    removeMessageList.add(hasMessage);
                }
            }
        }
        if(removeMessageList.size()>0){
            chatList.removeAll(removeMessageList);
        }
        this.chatList.addAll(chatList);
    }

    public void addChat(ChatMessage chatMessage) {
        lastCount = getCount();
        this.chatList.add(chatMessage);
    }
}
