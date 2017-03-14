//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hhkj.talkdata.api.request;

import com.hhkj.talkdata.api.base.RequestMessage;

import java.util.Date;

public class GetBreakdownMesRequest extends RequestMessage {
    private String name;
    private String reason;
    private Date date;

    public GetBreakdownMesRequest() {
    }

    public void createHeader() {
        super.createHeader();
        this.header.setAction("S002");
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
