package com.yousucc.database.tables.base;

import com.yousucc.ui.widget.indexlist.LetterUtil;

/**
 * Created by DELL on 2016/4/2.
 */
public class ComparableBean extends OrmTableBase implements Comparable<ComparableBean> {
    private char header = (char) -1;
    private String compareString = "";

    public ComparableBean(String compareString) {
        if (compareString != null && compareString.length() > 0) {
            this.initHeader(compareString.toUpperCase().charAt(0));
            this.compareString = StringToCompareString(compareString);
        }
    }

    private void initHeader(char upperChar) {
        if (Character.isDigit(upperChar)) {
            header = '#';
        } else {
            header = LetterUtil.getFirstPinYinChar(upperChar);
            if (header < 'A' || header > 'Z') {
                header = '#';
            }
        }
    }

    public char getHeader() {
        return header;
    }

    private String StringToCompareString(String toString) {
        if (toString == null) return "";
        String temp = "";
        for (char tempchar : toString.toCharArray()) {
            if (LetterUtil.isChinese(tempchar)) {
                String[] pinyins = LetterUtil.getFirstPinyin(tempchar);
                for (String pinyin : pinyins) {
                    temp += pinyin;
                }
            } else {
                temp += tempchar;
            }
        }
        temp = temp.toUpperCase();
        String newchararr = "";
        for (char tempchar : temp.toCharArray()) {
            if (!LetterUtil.isLetter(tempchar)) {
                tempchar = (char) (tempchar + 123);
            }
            newchararr += tempchar;
        }
        return newchararr;
    }

    @Override
    public int compareTo(ComparableBean another) {
        return compareString.compareTo(another.compareString);
    }
}
