package com.yousucc.database.tables;

import com.litesuits.orm.db.annotation.Table;
import com.yousucc.database.tables.base.OrmTableBase;

import java.util.Arrays;

/**
 * Created by SensYang on 2016/3/27 0027.
 */
@Table("conversation")
public class Conversation extends OrmTableBase{
    public static final String FROM = "_from";
    public static final String SESSIONID = "sessionId";
    /**
     * type 类型
     * 默认是people
     * person,group,recruit,business,news
     */
    private String conversationType;
    private String headUrls[];
    private int unreadCount;
    private String name;
    private String info;
    private String distance;
    private long time;
    private String _from;
    private String sessionId;

    public String getConversationType() {
        return conversationType;
    }

    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }

    public String[] getHeadUrls() {
        return headUrls;
    }

    public void setHeadUrls(String[] headUrls) {
        this.headUrls = headUrls;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFrom() {
        return _from;
    }

    public void setFrom(String from) {
        this._from = from;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "conversationType='" + conversationType + '\'' +
                ", headUrls=" + Arrays.toString(headUrls) +
                ", unreadCount=" + unreadCount +
                ", name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", distance='" + distance + '\'' +
                ", time=" + time +
                ", from='" + _from + '\'' +
                '}';
    }
}
