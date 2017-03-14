package com.hhkj.talkdata.api.request;

import com.hhkj.talkdata.api.base.RequestMessage;

/**
 * Created by litj on 2017/2/20.
 */

public class ClickStatisticsRequest extends RequestMessage {

    private String buryPointId;

    public ClickStatisticsRequest() {
    }

    public void createHeader() {
        super.createHeader();
        this.header.setAction("S006");
    }

    public String getBuryPointId() {
        return this.buryPointId;
    }

    public void setBuryPointId(String buryPointId) {
        this.buryPointId = buryPointId;
    }

}
