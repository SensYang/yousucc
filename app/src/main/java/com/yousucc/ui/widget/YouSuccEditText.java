package com.yousucc.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.yousucc.YouSuccApplication;

/**
 * Created by SensYang on 2016/3/24 0024.
 */
public class YouSuccEditText extends EditText {

    public YouSuccEditText(Context context) {
        super(context);
    }

    public YouSuccEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YouSuccEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(YouSuccApplication.getInstance().getTypeFace(), style);
    }
}
