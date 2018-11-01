package com.yousucc.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;

import com.yousucc.YouSuccApplication;

/**
 * Created by SensYang on 2016/3/24 0024.
 */
public class YouSuccButton extends Button {

    public YouSuccButton(Context context) {
        this(context, null);
    }

    public YouSuccButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YouSuccButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(YouSuccApplication.getInstance().getTypeFace(), style);
    }
}
