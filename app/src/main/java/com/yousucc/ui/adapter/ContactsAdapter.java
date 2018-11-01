package com.yousucc.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yousucc.R;
import com.yousucc.config.GlideRoundTransform;
import com.yousucc.database.tables.Contacts;
import com.yousucc.ui.activity.chat.ChatActivity;
import com.yousucc.ui.widget.indexlist.IndexerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DELL on 2016/4/2.
 */
public class ContactsAdapter extends IndexerAdapter implements AbsListView.OnItemClickListener {
    private List<Contacts> contactsList = new ArrayList<>();
    private GlideRoundTransform roundTransform;
    private Intent chatIntent;
    private Context context;

    public ContactsAdapter(Context context) {
        this.context = context;
        chatIntent = new Intent(context, ChatActivity.class);
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public Contacts getItem(int position) {
        return contactsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, String headerString) {
        ViewHolder viewHolder;
        if (roundTransform == null) {
            roundTransform = new GlideRoundTransform(parent.getContext());
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacts, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Contacts contacts = getItem(position);
        if (contacts != null) {
            Glide.with(viewHolder.imageIV.getContext())
                    .load(contacts.getHead())
                    .placeholder(R.drawable.default_head_boy)
                    .crossFade()
                    .centerCrop()
                    .transform(roundTransform)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.imageIV);
            viewHolder.nameTV.setText(contacts.getName());
        }
        if (headerString != null) {
            viewHolder.topInfoLL.setVisibility(View.VISIBLE);
            viewHolder.topInfoTV.setText(headerString);
        } else {
            viewHolder.topInfoLL.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void addContacts(Contacts contacts) {
        this.contactsList.add(contacts);
    }

    public void setContactsList(List<Contacts> contactsList) {
        this.contactsList = contactsList;
    }

    @Override
    public void notifyDataSetChanged() {
        Collections.sort(contactsList);
        super.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            return;
        }
        Contacts contacts = getItem(position - 1);
        chatIntent.putExtra("chatwith", contacts.getSessionId());
        context.startActivity(chatIntent);
    }

    class ViewHolder {
        @Bind(R.id.topInfoTV)
        TextView topInfoTV;
        @Bind(R.id.topInfoLL)
        LinearLayout topInfoLL;
        @Bind(R.id.imageIV)
        ImageView imageIV;
        @Bind(R.id.nameTV)
        TextView nameTV;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
