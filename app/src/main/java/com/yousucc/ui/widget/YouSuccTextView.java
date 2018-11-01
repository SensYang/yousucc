package com.yousucc.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yousucc.YouSuccApplication;

/**
 * Created by SensYang on 2016/3/24 0024.
 */
public class YouSuccTextView extends TextView {
    public YouSuccTextView(Context context) {
        super(context);
    }

    public YouSuccTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YouSuccTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(YouSuccApplication.getInstance().getTypeFace(), style);
    }
}
