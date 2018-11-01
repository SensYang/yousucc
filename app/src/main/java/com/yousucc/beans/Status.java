package com.yousucc.beans;


import java.io.Serializable;

/**
 * Created by SensYang on 2016/3/26 0026.
 */
public class Status implements Serializable {
    private int status;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "StatusBean{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                '}';
    }
}
