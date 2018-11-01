package com.yousucc.ui.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yousucc.R;
import com.yousucc.config.GlideRoundTransform;
import com.yousucc.database.LiteOrmDBUtil;
import com.yousucc.database.tables.ChatMessage;
import com.yousucc.database.tables.Conversation;
import com.yousucc.ui.activity.chat.ChatActivity;
import com.yousucc.ui.base.slidback.IntentUtils;
import com.yousucc.ui.widget.YouSuccTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SensYang on 2016/3/27 0027.
 */
public class ConversationAdapter extends BaseAdapter implements AbsListView.OnItemClickListener {
    private ArrayList<Conversation> conversationList = new ArrayList<>();
    private Intent chatIntent;
    private GlideRoundTransform roundTransform;

    public ConversationAdapter(AbsListView listView) {
        listView.setAdapter(this);
        listView.setOnItemClickListener(this);
        chatIntent = new Intent(listView.getContext(), ChatActivity.class);
        roundTransform = new GlideRoundTransform(listView.getContext());
    }

    @Override
    public int getCount() {
        return conversationList.size();
    }

    @Override
    public Conversation getItem(int position) {
        return conversationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Conversation conversation = conversationList.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //peision,group,recruit,business,news
        if (conversation.getUnreadCount() != 0) {
            holder.unreadTV.setVisibility(View.VISIBLE);
            holder.unreadTV.setText(conversation.getUnreadCount() + "");
        } else {
            holder.unreadTV.setVisibility(View.GONE);
        }
        holder.nameTV.setText(conversation.getName());
        holder.infoTV.setText(conversation.getInfo());
        holder.distanceTV.setText(conversation.getDistance());
        holder.timeTV.setText(conversation.getTime() + "");
        //person,group,recruit,business,news
        switch (conversation.getConversationType()) {
            case "person":
            case "group":
            case "recruit":
            case "business":
                String headUrl = conversation.getHeadUrls()[0];
                Log.e("headUrl--->", headUrl);
                Glide.with(holder.imageIV.getContext())
                        .load(headUrl)
                        .placeholder(R.drawable.default_head_boy)
                        .crossFade()
                        .centerCrop()
                        .transform(roundTransform)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageIV);
                break;
            case "news":
                Glide.with(holder.imageIV.getContext())
                        .load(R.drawable.conversation_news_icon)
                        .placeholder(R.drawable.default_head_boy)
                        .crossFade()
                        .centerCrop()
                        .transform(roundTransform)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageIV);
                break;
        }
        return convertView;
    }

    public void addConversation(Conversation conversation) {
        Conversation temp = null;
        for (Conversation conversation1 : conversationList) {
            if (conversation.getSessionId().equalsIgnoreCase(conversation1.getSessionId())) {
                temp = conversation1;
            }
        }
        if (temp != null)
            this.conversationList.remove(temp);
        this.conversationList.add(conversation);
    }

    public void addConversations(List<Conversation> conversationList) {
        this.conversationList.addAll(conversationList);
    }

    static class ViewHolder {
        @Bind(R.id.imageIV)
        ImageView imageIV;
        @Bind(R.id.unreadTV)
        YouSuccTextView unreadTV;
        @Bind(R.id.nameTV)
        YouSuccTextView nameTV;
        @Bind(R.id.infoTV)
        YouSuccTextView infoTV;
        @Bind(R.id.timeTV)
        YouSuccTextView timeTV;
        @Bind(R.id.distanceTV)
        YouSuccTextView distanceTV;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void updateConversation(List<ChatMessage> chatMessageList) {
        HashMap<String, ChatMessage> hashMap = new HashMap<>();
        for (ChatMessage chatMessage : chatMessageList) {
            hashMap.put(chatMessage.getSessionId(), chatMessage);
        }
        Set<String> keys = hashMap.keySet();
        for (String key : keys) {
            List<Conversation> conversationList = LiteOrmDBUtil.getInstance().getQueryByWhere(Conversation.class, Conversation.SESSIONID, new String[]{key});
            if (conversationList.size() > 0) {
                Conversation conversation = conversationList.get(0);
                addConversation(conversation);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Conversation conversation = conversationList.get(position);
        conversation.setUnreadCount(0);
        LiteOrmDBUtil.getInstance().update(conversation);
        notifyDataSetChanged();
        chatIntent.putExtra("chatwith", getItem(position).getSessionId());
        view.getContext().startActivity(chatIntent);
    }
}
