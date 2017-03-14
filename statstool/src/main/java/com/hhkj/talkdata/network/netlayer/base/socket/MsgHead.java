package com.hhkj.talkdata.network.netlayer.base.socket;

/**
 * Created by guold on 2016/7/4.
 */
public class MsgHead {
    //    协议版本	1byte	默认1
    byte protocolVersion;
    //    协议类型	1byte	默认1
    byte protocolType;
    //    报文版本	1byte	默认1
    byte msgVersion;
    //    请求类型	1byte	请求0，应答1
    byte reqVersion;
    //    报文类型	1byte	默认1，JSON
    byte msgType;
    //    预留1	1byte
    byte reserved1;
    //    预留2	1byte
    byte reserved2;
    //    预留3	1byte
    byte reserved3;
    //    序号	Int32	唯一，为方便请求和应答报文对应，推送报文默认0
    int msgId;
    //    功能号	Int32	10：登录,15：登出,19：心跳包,101：推送信息
//    int funCode;
    //    报文体长度	Int32	报文体的长度
    int contentLength;

    public byte getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(byte protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public byte getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(byte protocolType) {
        this.protocolType = protocolType;
    }

    public byte getMsgVersion() {
        return msgVersion;
    }

    public void setMsgVersion(byte msgVersion) {
        this.msgVersion = msgVersion;
    }

    public byte getReqVersion() {
        return reqVersion;
    }

    public void setReqVersion(byte reqVersion) {
        this.reqVersion = reqVersion;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public byte getReserved1() {
        return reserved1;
    }

    public void setReserved1(byte reserved1) {
        this.reserved1 = reserved1;
    }

    public byte getReserved2() {
        return reserved2;
    }

    public void setReserved2(byte reserved2) {
        this.reserved2 = reserved2;
    }

    public byte getReserved3() {
        return reserved3;
    }

    public void setReserved3(byte reserved3) {
        this.reserved3 = reserved3;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    /*public int getFunCode() {
        return funCode;
    }*/

    /*public void setFunCode(int funCode) {
        this.funCode = funCode;
    }*/

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public MsgHead() {
    }

    public MsgHead(byte protocolVersion, byte protocolType, byte msgVersion, byte reqVersion, byte msgType, byte reserved1, byte reserved2, byte reserved3, int msgId, int contentLength) {
        this.protocolVersion = protocolVersion;
        this.protocolType = protocolType;
        this.msgVersion = msgVersion;
        this.reqVersion = reqVersion;
        this.msgType = msgType;
        this.reserved1 = reserved1;
        this.reserved2 = reserved2;
        this.reserved3 = reserved3;
        this.msgId = msgId;
//        this.funCode = funCode;
        this.contentLength = contentLength;
    }
}
