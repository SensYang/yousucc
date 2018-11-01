package com.yousucc.database;

import com.yousucc.utils.DisplayUtil;

/**
 * Created by SensYang on 2016/4/9 0009.
 */
public enum PreferencesSetting {
    /**是否进入过引导页*/
    SETTING_HASGUIDE("hasguide", false),
    /**设置http的cookie信息*/
    SETTING_COOKIE("Cookie", ""),
    /**键盘高度*/
    SETTING_KEYBOARDHEIGHT("keyBoardHeight", 500),
    /**正在与谁聊天*/
    SETTING_CHATWITH("chatwith","")
    ;

    private final String settingName;
    private final Object defaultValue;

    PreferencesSetting(String settingName, Object defaultValue) {
        this.settingName = settingName;
        this.defaultValue = defaultValue;
    }

    /**
     * 获取字段名
     */
    public String getSettingName() {
        return settingName;
    }

    /**
     * 获取默认值
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**根据字段名字实例化*/
    public static PreferencesSetting fromSetting(String settingName) {
        PreferencesSetting[] values = values();
        int cc = values.length;
        for (int i = 0; i < cc; i++) {
            if (values[i].settingName == settingName) {
                return values[i];
            }
        }
        return null;
    }
}
