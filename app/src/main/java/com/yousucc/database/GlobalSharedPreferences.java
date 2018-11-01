package com.yousucc.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.yousucc.YouSuccApplication;

/**
 * Created by SensYang on 2016/03/31
 */
public class GlobalSharedPreferences {
    private static GlobalSharedPreferences globalSharedPreferences;
    /**
     * 参数配置信息
     */
    private static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    /**
     * filename
     */
    public static final String SHAREDPREFERENCES_GOLBAL = "yousucc_global";//标记用户的公共属性

    private GlobalSharedPreferences() {
    }

    public static GlobalSharedPreferences getInstance() {
        if (globalSharedPreferences == null) {
            globalSharedPreferences = new GlobalSharedPreferences();
            preferences = YouSuccApplication.getInstance().getSharedPreferences(SHAREDPREFERENCES_GOLBAL, Context.MODE_PRIVATE);
            editor = preferences.edit();
        }
        return globalSharedPreferences;
    }

    /**
     * 判断配置文件是否含有key值
     *
     * @param key
     * @return
     */
    public boolean isContainsKey(String key) {
        return preferences == null ? null : preferences.contains(key);
    }

    /**
     * 向配置文件存入boolean类型的键值对key——>value
     *
     * @param key
     * @param value
     */
    public void setBooleanPreferences(String key, boolean value) {
        if (editor != null) {
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    /**
     * 获取配置文件boolean类型的键值对key——>defValue
     *
     * @param key
     * @param defValue
     * @return
     */
    public boolean getBooleanPreferences(String key, boolean defValue) {
        return preferences == null ? null : preferences.getBoolean(key, defValue);
    }

    /**
     * 向配置文件添加String类型的键值对key——>value
     *
     * @param key
     * @param value
     */
    public void setStringPreferences(String key, String value) {
        if (editor != null) {
            editor.putString(key, value);
            editor.commit();
        }
    }

    /**
     * 获取配置文件String类型的键值对key——>defValue
     *
     * @param key
     * @param defValue
     * @return
     */
    public String getStringPreferences(String key, String defValue) {
        return preferences == null ? null : preferences.getString(key, defValue);
    }

    /**
     * 向配置文件添加int类型的键值对key——>value
     *
     * @param key
     * @param value
     */
    public void setIntPreferences(String key, int value) {
        if (editor != null) {
            editor.putInt(key, value);
            editor.commit();
        }
    }

    /**
     * 获取配置文件int类型的键值对key——>defValue
     *
     * @param key
     * @param defValue
     * @return
     */
    public int getIntPreferences(String key, int defValue) {
        return preferences == null ? null : preferences.getInt(key, defValue);
    }
}
