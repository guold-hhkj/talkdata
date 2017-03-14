package com.hhkj.talkdata.network.netlayer.base.socket;

/**
 * Created by guold on 2016/7/4.
 */
public class ExeInfo extends Exception {
    int code;
    String desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ExeInfo() {
    }

    public ExeInfo(String detailMessage) {
        super(detailMessage);
    }

    public ExeInfo(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ExeInfo(Throwable throwable) {
        super(throwable);
    }

    public ExeInfo(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
