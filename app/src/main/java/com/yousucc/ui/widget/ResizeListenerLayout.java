package com.yousucc.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.yousucc.utils.Log;

import java.util.ArrayList;

/**
 * Created by SensYang on 2016/4/16 0016.
 */
public class ResizeListenerLayout extends LinearLayout {
    private int lastHeightMeasureSpec = 0;
    private int lastWidthMeasureSpec = 0;

    public ResizeListenerLayout(Context context) {
        super(context);
    }

    public ResizeListenerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizeListenerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (lastHeightMeasureSpec != heightMeasureSpec) {
            if (lastHeightMeasureSpec != 0)
                if (lastHeightMeasureSpec > heightMeasureSpec) {
                    Log.e("ResizeListenerLayout--->", "矮了");
                    for (OnResizeListener onResizeListener : onResizeListenerList) {
                        onResizeListener.onHeightToBigger(false, lastHeightMeasureSpec - heightMeasureSpec);
                    }
                } else if (lastHeightMeasureSpec < heightMeasureSpec) {
                    Log.e("ResizeListenerLayout--->", "高了");
                    for (OnResizeListener onResizeListener : onResizeListenerList) {
                        onResizeListener.onHeightToBigger(true, heightMeasureSpec - lastHeightMeasureSpec);
                    }
                }
            lastHeightMeasureSpec = heightMeasureSpec;
        }
        if (lastWidthMeasureSpec != widthMeasureSpec) {
            if (lastWidthMeasureSpec != 0)
                if (lastWidthMeasureSpec > widthMeasureSpec) {
                    Log.e("ResizeListenerLayout--->", "瘦了");
                    for (OnResizeListener onResizeListener : onResizeListenerList) {
                        onResizeListener.onWidthToBigger(false, lastWidthMeasureSpec - widthMeasureSpec);
                    }
                } else if (lastWidthMeasureSpec < widthMeasureSpec) {
                    Log.e("ResizeListenerLayout--->", "胖了");
                    for (OnResizeListener onResizeListener : onResizeListenerList) {
                        onResizeListener.onWidthToBigger(true, widthMeasureSpec - lastWidthMeasureSpec);
                    }
                }
            lastWidthMeasureSpec = widthMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 尺寸监听器集合
     */
    private ArrayList<OnResizeListener> onResizeListenerList = new ArrayList<>(0);

    /**
     * 添加尺寸监听器
     */
    public void addResizeListener(OnResizeListener onResizeListener) {
        onResizeListenerList.add(onResizeListener);
    }

    /**
     * 删除尺寸监听器
     */
    public void removeResizeListener(OnResizeListener onResizeListener) {
        onResizeListenerList.remove(onResizeListener);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        for (OnResizeListener onResizeListener : onResizeListenerList) {
            onResizeListener.onAfterSizeChanged(width, height, oldWidth, oldHeight);
        }
        super.onSizeChanged(width, height, oldWidth, oldHeight);
    }

    public interface OnResizeListener {

        void onAfterSizeChanged(int width, int height, int oldWidth, int oldHeight);

        void onWidthToBigger(boolean isBigger, int sizeDiff);

        void onHeightToBigger(boolean isBigger, int sizeDiff);
    }
}
