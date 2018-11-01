package com.yousucc.utils;

import android.os.Build;

/**
 * SDK版本工具
 * com.miquan.common in ECDemo_Android
 * Created by Jorstin on 2015/6/23.
 */
public class SDKVersionUtils {

    /**
     * 小于版本
     */
    public static boolean isSmallerVersion(int version) {
        return (Build.VERSION.SDK_INT < version);
    }

    /**
     * 不小于版本
     */
    public static boolean isGreaterorEqual(int version) {
        return (Build.VERSION.SDK_INT >= version);
    }

    /**
     * 不大于版本
     */
    public static boolean isSmallerorEqual(int version) {
        return (Build.VERSION.SDK_INT <= version);
    }
}
