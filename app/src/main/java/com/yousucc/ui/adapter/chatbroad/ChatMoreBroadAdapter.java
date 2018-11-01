package com.yousucc.ui.adapter.chatbroad;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yousucc.R;
import com.yousucc.ui.widget.ChatControlView;
import com.yousucc.utils.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SensYang on 2016/4/16 0016.
 */
public class ChatMoreBroadAdapter extends PagerAdapter {
    private TypedArray moreImageResource;
    private String[] moreTextResource;
    private List<View> controlBroadList = new ArrayList<>(2);
    private LayoutInflater inflater;

    //TODO
    public ChatMoreBroadAdapter(Context context, ChatControlView.ChatType type) {
        inflater = LayoutInflater.from(context);
        switch (type) {
            case PEOPLE:
                moreImageResource = context.getResources().obtainTypedArray(R.array.peopleChatMoreImageResources);
                moreTextResource = context.getResources().getStringArray(R.array.peopleChatMoreTextResources);
                break;
            case GROUP:
                moreImageResource = context.getResources().obtainTypedArray(R.array.peopleChatMoreImageResources);
                moreTextResource = context.getResources().getStringArray(R.array.peopleChatMoreTextResources);
                break;
            case BUSINESS:
                moreImageResource = context.getResources().obtainTypedArray(R.array.peopleChatMoreImageResources);
                moreTextResource = context.getResources().getStringArray(R.array.peopleChatMoreTextResources);
                break;
            default:
                moreImageResource = context.getResources().obtainTypedArray(R.array.peopleChatMoreImageResources);
                moreTextResource = context.getResources().getStringArray(R.array.peopleChatMoreTextResources);
                break;
        }
        initView(4, 2);
    }

    private void initView(int oneLenght, int maxLine) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        for (int i = 0; i < moreTextResource.length; i += oneLenght) {
            if (i % (oneLenght * maxLine) == 0)
                controlBroadList.add(inflater.inflate(R.layout.broad_chat_items, null));
            initView(i, oneLenght, (LinearLayout) ((ViewGroup) controlBroadList.get(i / (oneLenght * maxLine))).getChildAt(i % (oneLenght * maxLine) / oneLenght), layoutParams);
        }
    }

    private void initView(int start, int oneLenght, LinearLayout linearLayout, LinearLayout.LayoutParams layoutParams) {
        int to = start + oneLenght > moreTextResource.length ? moreTextResource.length : start + oneLenght;
        linearLayout.setWeightSum(oneLenght);
        for (; start < to; start++) {
            View view = inflater.inflate(R.layout.item_chat_board, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            TextView textView = (TextView) view.findViewById(R.id.textView);
            imageView.setImageDrawable(moreImageResource.getDrawable(start));
            textView.setText(moreTextResource[start]);
            view.setLayoutParams(layoutParams);
            view.setId(start);
            view.setTag(moreTextResource[start]);
            view.setOnClickListener(broadItemClickListener);
            linearLayout.addView(view);
        }
    }

    private View.OnClickListener broadItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onBroadItemClickListener != null) {
                onBroadItemClickListener.onClickBroadItem(v.getId(), (String) v.getTag());
            }
        }
    };

    @Override
    public int getCount() {
        return controlBroadList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(controlBroadList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(controlBroadList.get(position));
        return controlBroadList.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    ////////////////////////////////////////////////////
    /**
     * 按钮点击监听器
     */
    private OnBroadItemClickListener onBroadItemClickListener;

    /**
     * 设置按钮点击监听器
     */
    public void setOnBroadItemClickListener(OnBroadItemClickListener onBroadItemClickListener) {
        this.onBroadItemClickListener = onBroadItemClickListener;
    }

    /**
     * 按钮点击监听器
     */
    public interface OnBroadItemClickListener {
        void onClickBroadItem(int position, String name);
    }
}
