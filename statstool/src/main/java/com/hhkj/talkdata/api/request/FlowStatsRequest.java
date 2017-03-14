package com.hhkj.talkdata.api.request;

import com.hhkj.talkdata.api.base.RequestMessage;

/**
 * Created by litj on 2017/2/20.
 */

public class FlowStatsRequest extends RequestMessage{

    private String flowId;
    private String useLength;

    public FlowStatsRequest() {
    }

    public void createHeader() {
        super.createHeader();
        this.header.setAction("S007");
    }

    public String getFlowId() {
        return this.flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getUseLength() {
        return this.useLength;
    }

    public void setUseLength(String useLength) {
        this.useLength = useLength;
    }

}
