package com.yousucc.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by SensYang on 2016/4/18 0018.
 */
public class ChatListView extends ListView {
    public ChatListView(Context context) {
        super(context);
    }

    public ChatListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        requestFocus();
        return super.onInterceptTouchEvent(ev);
    }
}
