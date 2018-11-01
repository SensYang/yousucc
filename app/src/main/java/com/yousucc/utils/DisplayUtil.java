package com.yousucc.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.yousucc.YouSuccApplication;

public class DisplayUtil {

    private static DisplayMetrics displaysMetrics = null;
    private static float scale = -1.0f;

    public static DisplayMetrics getDisplayMetrics() {
        if (null == displaysMetrics) {
            displaysMetrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) YouSuccApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(displaysMetrics);
        }
        return displaysMetrics;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getHeight() {
        if (null == displaysMetrics) {
            getDisplayMetrics();
        }
        return displaysMetrics.heightPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getWidth() {
        if (null == displaysMetrics) {
            getDisplayMetrics();
        }
        return displaysMetrics.widthPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        if (scale < 0) {
            if (null == displaysMetrics) {
                getDisplayMetrics();
            }
            scale = displaysMetrics.density;
        }

        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        if (scale < 0) {
            if (null == displaysMetrics) {
                getDisplayMetrics();
            }
            scale = displaysMetrics.density;
        }
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从sp 的单位 转成为 dp
     */
    public static int sp2px(float spValue) {
        if (null == displaysMetrics) {
            getDisplayMetrics();
        }
        float fontScale = displaysMetrics.scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
//    public static Bitmap voiceBitmap;
//    public static void getShotBitmap(Activity activity) {
//        // 获取windows中最顶层的view
//        View view = activity.getWindow().getDecorView();
//        view.buildDrawingCache();
//
//        // 获取状态栏高度
//        Rect rect = new Rect();
//        view.getWindowVisibleDisplayFrame(rect);
//        int statusBarHeights = rect.top;
//        Display display = activity.getWindowManager().getDefaultDisplay();
//
//        // 获取屏幕宽和高
//        int widths = display.getWidth();
//        int heights = display.getHeight();
//
//        // 允许当前窗口保存缓存信息
//        view.setDrawingCacheEnabled(true);
//
//        // 去掉状态栏
//        voiceBitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeights, widths, heights - statusBarHeights);
//        // 销毁缓存信息
//        view.destroyDrawingCache();
//    }
}
