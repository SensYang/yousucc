package com.yousucc.ui.adapter.chatbroad;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emojicon.EmojiconTextView;
import com.emojicon.RepeatListener;
import com.emojicon.emoji.Emojicon;
import com.emojicon.emoji.PeopleEmoji;
import com.yousucc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SensYang on 2016/4/16 0016.
 */
public class ChatFaceAdapter extends PagerAdapter {
    private List<View> faceBroadList = new ArrayList<>(2);
    private LayoutInflater inflater;
    private Emojicon[] emojicons = new Emojicon[0];

    public ChatFaceAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        emojicons = PeopleEmoji.DATA;
        initView(7, 3);
    }

    private void initView(int oneLenght, int maxLine) {
        int pageArea = oneLenght * maxLine;//一页的图标容量
        int deleteIconCount = emojicons.length / (pageArea - 1) + 1;//back图标的数量
        int count = emojicons.length + deleteIconCount;//包含删除按钮 总共的emoji数量
        int pageIndex = -1;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        LinearLayout.LayoutParams emojiLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        emojiLayoutParams.gravity = Gravity.CENTER;
        for (int i = 0; i < count; i += oneLenght) {
            if (i % pageArea == 0) {
                faceBroadList.add(inflater.inflate(R.layout.broad_face_items, null));
                pageIndex++;
            }
            LinearLayout linearLayout = (LinearLayout) ((ViewGroup) faceBroadList.get(i / (oneLenght * maxLine))).getChildAt(i % (oneLenght * maxLine) / oneLenght);
            linearLayout.setWeightSum(oneLenght);
            initView(i, pageIndex, oneLenght, pageArea, linearLayout, layoutParams, emojiLayoutParams);
        }
    }

    private void initView(int start, int pageIndex, int oneLenght, int pageArea, LinearLayout linearLayout, LinearLayout.LayoutParams layoutParams, LinearLayout.LayoutParams emojiLayoutParams) {
        start -= pageIndex;
        int to = start + oneLenght;
        for (; start < to; start++) {
            View view = inflater.inflate(R.layout.item_face_board, null);
            EmojiconTextView emojiconTextView = (EmojiconTextView) view.findViewById(R.id.emojiconTextView);
            View backIV = view.findViewById(R.id.backIV);
            if ((start + pageIndex) % pageArea == pageArea - 1 || start == emojicons.length) {
                ((ViewGroup) view).removeView(emojiconTextView);
                backIV.setVisibility(View.VISIBLE);
                backIV.setLayoutParams(emojiLayoutParams);
                view.setTag("D");
                view.setId(start);
                view.setLayoutParams(layoutParams);
                view.setOnTouchListener(repeatListener);
                linearLayout.addView(view);
                break;
            }
            ((ViewGroup) view).removeView(backIV);
            emojiconTextView.setLayoutParams(emojiLayoutParams);
            String emojiString = emojicons[start].getEmoji();
            emojiconTextView.setText(emojiString);
            view.setTag(emojiString);

            view.setId(start);
            view.setLayoutParams(layoutParams);
            view.setOnClickListener(emojiItemClickListener);
            linearLayout.addView(view);
        }
    }

    /**
     * 重复点击事件
     */
    private RepeatListener repeatListener = new RepeatListener(1000, 50, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onFaceClickListener != null) {
                onFaceClickListener.onClickFace(v.getId(), (CharSequence) v.getTag());
            }
        }
    });

    private View.OnClickListener emojiItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onFaceClickListener != null) {
                onFaceClickListener.onClickFace(v.getId(), (CharSequence) v.getTag());
            }
        }
    };

    @Override
    public int getCount() {
        return faceBroadList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(faceBroadList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(faceBroadList.get(position));
        return faceBroadList.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 表情点击监听器
     */
    private OnFaceClickListener onFaceClickListener;

    /**
     * 设置表情的点击监听器
     */
    public void setOnFaceClickListener(OnFaceClickListener onFaceClickListener) {
        this.onFaceClickListener = onFaceClickListener;
    }

    /**
     * 表情的点击监听器
     */
    public interface OnFaceClickListener {
        void onClickFace(int index, CharSequence text);
    }
}
