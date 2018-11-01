package com.yousucc.utils;

import android.text.TextUtils;

/**
 * Created by zl on 2014/12/2.
 */
public class StringUtil {

	/**
	 * 判断给定字符串是否空白串。
	 *
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || 0 == TextUtils.getTrimmedLength(input))
			return true;
		return false;
	}

	public static String replaceSpechars(String str){
		if(null != str) {
			str = str.replace("&", "&amp;");
			str = str.replace("<", "&lt;");
			str = str.replace(">", "&gt;");
			return str;
		} else{
			return null;
		}
	}


	public static String toTopicStr(String str){
		if(!isEmpty(str)){
			return "#" + str + "#";
		}
		return str;
	}
}
