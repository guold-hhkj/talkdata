//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hhkj.talkdata.api.base;

import java.io.Serializable;

public class ResponseMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    ResponseMessage.DATAS_SPECS datasSpecs;
    protected Header header;

    public ResponseMessage() {
        this.datasSpecs = ResponseMessage.DATAS_SPECS.table_with_head;
    }

    public ResponseMessage(RequestMessage request) {
        this.datasSpecs = ResponseMessage.DATAS_SPECS.table_with_head;
        if(request.getHeader() != null) {
            this.createHeader(request.getHeader());
        }

    }

    public void createHeader() {
        this.header = new Header();
        this.header.setMsgtype(Integer.valueOf(1));
    }

    public void createHeader(Header header) {
        if(this.header == null) {
            this.header = new Header(header);
        } else {
            this.header.create(header);
            this.header.setSendingtime(Header.getDateTime());
        }

        this.header.setMsgtype(Integer.valueOf(1));
    }

    public void createError(Header header) {
        this.header.setCode(header.getCode());
        this.header.setMsg(header.getMsg());
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public static long getSerialVersionUID() {
        return 1L;
    }

    public ResponseMessage.DATAS_SPECS getDatasSpecs() {
        return this.datasSpecs;
    }

    public void setDatasSpecs(ResponseMessage.DATAS_SPECS datasSpecs) {
        this.datasSpecs = datasSpecs;
    }

    public static enum DATAS_SPECS {
        table_with_head,
        table_raw,
        table_complex;

        private DATAS_SPECS() {
        }
    }
}
