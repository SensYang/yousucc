package com.yousucc.database.tables;

import com.yousucc.database.tables.base.ComparableBean;

/**
 * Created by DELL on 2016/4/2.
 */
public class Contacts extends ComparableBean {
    private String name;
    private String head = "http://img4.duitang.com/uploads/item/201502/13/20150213004413_Qdzm5.thumb.700_0.jpeg";
    private String sessionId;

    public Contacts(String name) {
        super(name);
        this.name = name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Contacts(String name, String head) {
        super(name);
        this.name = name;
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "name='" + name + '\'' +
                ", head='" + head + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
