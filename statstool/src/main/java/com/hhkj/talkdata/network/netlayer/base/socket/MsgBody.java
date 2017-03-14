package com.hhkj.talkdata.network.netlayer.base.socket;

/**
 * Created by guold on 2016/7/4.
 */
public class MsgBody {
    //报文体	byte[]	JSON字符串
    private byte[] content;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setContent(String content){
        this.content = content.getBytes();
    }

    @Override
    public String toString() {
        String tmp = new String(content);
        return tmp;
    }

    public MsgBody() {
    }

    public MsgBody(byte[] content) {
        this.content = content;
    }

    public MsgBody(String jsonStr){
        content = jsonStr.getBytes();
    }
}
