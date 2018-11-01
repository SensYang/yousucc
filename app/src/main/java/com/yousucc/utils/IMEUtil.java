package com.yousucc.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Sens on 2015/2/6 0006.
 */
public class IMEUtil implements ViewTreeObserver.OnGlobalLayoutListener {
    private static IMEUtil instance;

    public static IMEUtil getInstance() {
        if (instance == null) instance = new IMEUtil();
        return instance;
    }

    /**
     * 关闭输入法
     */
    public void hideIME(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 关闭输入法
     */
    public void closeIME(Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        try {
            View focusView = ((Activity) context).getCurrentFocus();
            if (manager != null && manager.isActive() && null != focusView) {
                manager.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示输入法
     */
    public void showIME(Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    /**
     * 获取键盘状态
     */
    public boolean isActiveState(Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return manager.isActive();
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard(View view) {
        ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     */
    public void showSoftKeyboard(View view) {
        ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    private View rootView;//activity的根视图
    private int rootViewVisibleHeight;//纪录根视图的显示高度
    private OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener;

    public void setOnSoftKeyBoardChangeListener(View view, OnSoftKeyBoardChangeListener softKeyBoardChangeListener) {
        onSoftKeyBoardChangeListener = softKeyBoardChangeListener;
        //获取根视图
        rootView = view.getRootView();
        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (rootView == null) return;
        //获取当前根视图在屏幕上显示的大小
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        int visibleHeight = r.height();
        if (rootViewVisibleHeight == 0) {
            rootViewVisibleHeight = visibleHeight;
            return;
        }

        //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
        if (rootViewVisibleHeight == visibleHeight) {
            return;
        }

        //根视图显示高度变小超过20，可以看作软键盘显示了
        if (rootViewVisibleHeight > visibleHeight) {
            if (onSoftKeyBoardChangeListener != null) {
                onSoftKeyBoardChangeListener.keyBoardShow(rootViewVisibleHeight - visibleHeight);
            }
            rootViewVisibleHeight = visibleHeight;
            return;
        }

        //根视图显示高度变大超过20，可以看作软键盘隐藏了
        if (visibleHeight > rootViewVisibleHeight) {
            if (onSoftKeyBoardChangeListener != null) {
                onSoftKeyBoardChangeListener.keyBoardHide(visibleHeight - rootViewVisibleHeight);
            }
            rootViewVisibleHeight = visibleHeight;
            return;
        }
    }

    public interface OnSoftKeyBoardChangeListener {
        void keyBoardShow(int height);

        void keyBoardHide(int height);
    }
}
