package com.yousucc.ui.base.slidback;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.yousucc.R;
import com.yousucc.ui.base.BaseActivity;

/**
 * Created by chenjishi on 14-3-17.
 */
public class SlidingActivity extends BaseActivity implements SlidingLayout.SlidingListener {
    private View mPreview;
    private float mInitOffset;

    private String mBitmapId;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.slide_layout);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        LayoutInflater inflater = LayoutInflater.from(this);
        mInitOffset = -(1.f / 3) * metrics.widthPixels;

        mPreview = findViewById(R.id.iv_preview);
        FrameLayout contentView = (FrameLayout) findViewById(R.id.content_view);

        contentView.addView(inflater.inflate(layoutResID, null));

        final SlidingLayout slideLayout = (SlidingLayout) findViewById(R.id.slide_layout);
        slideLayout.setShadowResource(R.drawable.sliding_back_shadow);
        slideLayout.setSlidingListener(this);
        slideLayout.setEdgeSize((int) (metrics.density * 20));

        mBitmapId = getIntent().getExtras().getString("bitmap_id");
        Bitmap bitmap = IntentUtils.getInstance().getBitmap(mBitmapId);
        if (null != bitmap) {
            if (Build.VERSION.SDK_INT >= 16) {
                mPreview.setBackground(new BitmapDrawable(null, bitmap));
            } else {
                mPreview.setBackground(new BitmapDrawable(null, bitmap));
            }
            IntentUtils.getInstance().setIsDisplayed(mBitmapId, true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IntentUtils.getInstance().setIsDisplayed(mBitmapId, false);
        mPreview = null;
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        if (slideOffset <= 0) {
        } else if (slideOffset < 1) {
            mPreview.setTranslationX(mInitOffset * (1 - slideOffset));
        } else {
            mPreview.setTranslationX(0);
            finish();
            overridePendingTransition(0, 0);
        }
    }
}
