package com.hhkj.talkdata.network.netlayer.base.socket;

/**
 * Created by guold on 2016/7/5.
 * 消息模板，更容易的生成消息
 */
public class MsgTamplate {
    private static MsgCounter msgCounter = new MsgCounter();
    public static Msg newMsg(){
        return new Msg((byte)1,(byte)1,(byte)1,(byte)0,(byte)1,(byte)1,(byte)1,(byte)1,msgCounter.getNum(),0,null);
    }

    public static Msg newMsg(String jsonData){
        Msg tmp = newMsg();
//        tmp.setFunCode(function);
        MsgBody msgBody = new MsgBody(jsonData);
        tmp.setContentLength(msgBody.getContent().length);
        tmp.setMsgBody(msgBody);
        return tmp;
    }
}
