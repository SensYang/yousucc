package com.yousucc.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * Created by SensYang on 2016/4/7 0007.
 */
public class BitmapUtil {
    /**
     * Drawable转Bitmap
     */
    public static Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable == null) return null;
        return drawableToBitamp(drawable, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    public static Bitmap drawableToBitamp(Drawable drawable, int width, int height) {
        if (drawable == null) return null;
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

}
