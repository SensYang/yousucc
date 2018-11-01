package com.yousucc.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yousucc.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Xml布局中如果不设id,初始默认id为{@link R.id#topNavigationBar}
 * Created by Orgtec on 2014/12/30.
 */
public class TopNavigationBar extends RelativeLayout {
    @Bind(R.id.leftBtn)
    YouSuccButton leftBtn;
    @Bind(R.id.leftIV)
    ImageView leftIV;
    @Bind(R.id.left)
    FrameLayout left;
    @Bind(R.id.centerIV)
    ImageView centerIV;
    @Bind(R.id.centerTV)
    YouSuccTextView centerTV;
    @Bind(R.id.center)
    RelativeLayout center;
    @Bind(R.id.rightBtn)
    YouSuccButton rightBtn;
    @Bind(R.id.rightIV)
    ImageView rightIV;
    @Bind(R.id.right)
    FrameLayout right;

    public TopNavigationBar(Context context) {
        this(context, null, 0);
    }

    public TopNavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopNavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.widget_top_navigation_bar, this);
        ButterKnife.bind(this);
        // 初始化属性
        this.initializeAttributes(context, attrs);
    }

    private void initializeAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopNavigationBar);
        boolean visibility;
        int resourceId;

        visibility = typedArray.getBoolean(R.styleable.TopNavigationBar_leftVisible, true);
        resourceId = typedArray.getResourceId(R.styleable.TopNavigationBar_leftBackground, 0);
        this.leftBtn.setText(typedArray.getText(R.styleable.TopNavigationBar_leftText));
        if (!visibility) {
            this.leftBtn.setVisibility(View.GONE);
            this.leftIV.setVisibility(View.GONE);
        }
        if (resourceId != 0) {
            this.leftIV.setBackgroundResource(resourceId);
        }

        visibility = typedArray.getBoolean(R.styleable.TopNavigationBar_centerVisible, true);
        resourceId = typedArray.getResourceId(R.styleable.TopNavigationBar_centerBackground, 0);
        this.centerTV.setText(typedArray.getText(R.styleable.TopNavigationBar_centerText));
        if (!visibility) {
            this.centerTV.setVisibility(View.GONE);
            this.centerIV.setVisibility(View.GONE);
        }
        if (resourceId != 0) {
            this.centerIV.setBackgroundResource(resourceId);
        }

        visibility = typedArray.getBoolean(R.styleable.TopNavigationBar_rightVisible, true);
        resourceId = typedArray.getResourceId(R.styleable.TopNavigationBar_rightBackground, 0);
        this.rightBtn.setText(typedArray.getText(R.styleable.TopNavigationBar_rightText));
        if (!visibility) {
            this.rightBtn.setVisibility(View.GONE);
            this.rightIV.setVisibility(View.GONE);
        }
        if (resourceId != 0) {
            this.rightIV.setBackgroundResource(resourceId);
        }
        typedArray.recycle();
    }

    public YouSuccButton getLeftBtn() {
        return leftBtn;
    }

    public ImageView getLeftIV() {
        return leftIV;
    }

    public ImageView getCenterIV() {
        return centerIV;
    }

    public YouSuccTextView getCenterTV() {
        return centerTV;
    }

    public YouSuccButton getRightBtn() {
        return rightBtn;
    }

    public ImageView getRightIV() {
        return rightIV;
    }

    public void setTitleText(CharSequence text) {
        if (this.centerTV != null) this.centerTV.setText(text);
    }

    public void setRightText(CharSequence text) {
        if (this.rightBtn != null) this.rightBtn.setText(text);
    }

    public void setRightImageResource(int resId) {
        if (this.rightIV != null) {
            this.rightIV.setImageResource(resId);
        }
    }
}
