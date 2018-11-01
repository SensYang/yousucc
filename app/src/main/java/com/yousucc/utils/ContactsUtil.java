package com.yousucc.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cxd on 2015/1/22.
 */


public class ContactsUtil {
    /**
     * 获取库Phon表字段
     **/
    public static final String[] PHONES_PROJECTION = new String[]{
            Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID};

    /**
     * 联系人显示名称
     **/
    public static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**
     * 电话号码
     **/
    public static final int PHONES_NUMBER_INDEX = 1;

    /**
     * 电话号码字符串
     **/
    private static StringBuffer buffer = new StringBuffer();

    /**
     * 判断输入字符串的是否为手机号
     */
    public static boolean isMobileNO(String mobiles) {
        if (mobiles == null || mobiles.length() == 0) return false;
        Pattern p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断输入的字符串是否为邮箱地址
     */
    public static boolean isEmail(String email) {
        if (email == null || email.length() == 0) return false;
        Pattern p = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 判断输入的字符串是否为邮政编码
     */
    public static boolean isPost(String post) {
        if (post == null || post.length() == 0) return false;
        Pattern p = Pattern.compile("[0-9]\\d{5}(?!\\d)");
        Matcher m = p.matcher(post);
        return m.matches();
    }

    /**
     * 得到手机通讯录联系人信息
     */
    public static Map<String, String> getPhoneContacts(Context mContext) {

        Map<String, String> contacts_map = new HashMap<String, String>();
        ContentResolver resolver = mContext.getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                phoneNumber = to_phone_num(phoneNumber);

                // 当手机号码为空的或者为空字段 跳过当前循环
                if (!isMobileNO(phoneNumber)) {
                    continue;
                } else {
                    // 得到联系人名称
                    String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                    contacts_map.put(phoneNumber, contactName);
                }
            }
            phoneCursor.close();
        }
        return contacts_map;
    }

    /**
     * 转化字符串为手机号格式
     */
    private static String to_phone_num(String str) {
        if (buffer.length() > 0)
            buffer.delete(0, buffer.length());

        if (str.indexOf("+86") != -1)
            str = str.replace("+86", "");
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9')
                buffer.append(str.charAt(i));
        return buffer.toString();
    }
}
