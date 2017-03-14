//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hhkj.talkdata.api.request;


import com.hhkj.talkdata.api.base.RequestMessage;

public class DeviceMesRequest extends RequestMessage {
    public DeviceMesRequest() {
    }

    public void createHeader() {
        super.createHeader();
        this.header.setAction("S005");
    }
}
