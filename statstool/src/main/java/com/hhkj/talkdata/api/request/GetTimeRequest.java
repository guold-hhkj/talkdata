//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hhkj.talkdata.api.request;


import com.hhkj.talkdata.api.base.RequestMessage;

public class GetTimeRequest extends RequestMessage {
    private String time;
    private String ip;

    public GetTimeRequest() {
    }

    public void createHeader() {
        super.createHeader();
        this.header.setAction("S001");
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
