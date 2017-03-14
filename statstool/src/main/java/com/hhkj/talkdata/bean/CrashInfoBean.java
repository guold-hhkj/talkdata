package com.hhkj.talkdata.bean;

import java.io.Serializable;

/**
 * 奔溃信息
 * Created by litj on 2017/1/13.
 */

public class CrashInfoBean implements Serializable {

    private long time;
    private String crashType;
    private String crashStackInfo;

    public CrashInfoBean(){}

    public String getCrashStackInfo() {
        return crashStackInfo;
    }

    public void setCrashStackInfo(String crashStackInfo) {
        this.crashStackInfo = crashStackInfo;
    }

    public String getCrashType() {
        return crashType;
    }

    public void setCrashType(String crashType) {
        this.crashType = crashType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
