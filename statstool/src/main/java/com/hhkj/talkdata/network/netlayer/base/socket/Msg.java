package com.hhkj.talkdata.network.netlayer.base.socket;

/**
 * Created by guold on 2016/7/4.
 */
public class Msg extends MsgHead{
    MsgBody msgBody;

    public MsgBody getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(MsgBody msgBody) {
        this.msgBody = msgBody;
    }

    public Msg() {
    }

    public Msg(byte[] bytes) {
        if(bytes.length>=20){
            setProtocolVersion(bytes[0]);
            setProtocolType(bytes[1]);
            setMsgVersion(bytes[2]);
            setReqVersion(bytes[3]);
            setMsgType(bytes[4]);
            setReserved1(bytes[5]);
            setReserved2(bytes[6]);
            setReserved3(bytes[7]);
            byte[] tmp= new byte[4];
            System.arraycopy(bytes,5,tmp,0,tmp.length);
            setMsgId(byteArrayToInt(tmp));
            System.arraycopy(bytes,12,tmp,0,tmp.length);
//            setFunCode(byteArrayToInt(tmp));
            System.arraycopy(bytes,16,tmp,0,tmp.length);
            System.arraycopy(bytes,5,tmp,0,tmp.length);
            setContentLength(byteArrayToInt(bytes));
        }
    }

    public Msg(byte protocolVersion, byte protocolType, byte msgVersion, byte reqVersion, byte msgType, byte reserved1, byte reserved2, byte reserved3, int msgId, int contentLength, MsgBody msgBody) {
        super(protocolVersion, protocolType, msgVersion, reqVersion, msgType, reserved1, reserved2, reserved3, msgId, contentLength);
        this.msgBody = msgBody;
    }

    public byte[] toBytes(){
        byte[] head = new byte[5];
        head[0]=getProtocolVersion();
        head[1]=getProtocolType();
        head[2]=getMsgVersion();
        head[3]=getReqVersion();
        head[4]=getMsgType();
        /*head[5]=getReserved1();
        head[6]=getReserved2();
        head[7]=getReserved3();
        byte[] tmp = intToByteArray(getMsgId());
        System.arraycopy(tmp,0,head,8,tmp.length);
        tmp = intToByteArray(getFunCode());
        System.arraycopy(tmp,0,head,12,tmp.length);
        tmp = intToByteArray(getContentLength());
        System.arraycopy(tmp,0,head,16,tmp.length);*/

        byte[] all = new byte[head.length+getContentLength()];
        System.arraycopy(head,0,all,0,head.length);
        System.arraycopy(getMsgBody().getContent(),0,all,head.length,getMsgBody().getContent().length);

        return all;
    }

    public static int byteArrayToInt(byte[] b){
        byte[] a = new byte[4];
        int i = a.length - 1,j = b.length - 1;
        for (; i >= 0 ; i--,j--) {//从b的尾部(即int值的低位)开始copy数据
            if(j >= 0)
                a[i] = b[j];
            else
                a[i] = 0;//如果b.length不足4,则将高位补0
        }
        int v0 = (a[0] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v1 = (a[1] & 0xff) << 16;
        int v2 = (a[2] & 0xff) << 8;
        int v3 = (a[3] & 0xff) ;
        return v0 + v1 + v2 + v3;
    }

    public static int intToByteArray(byte[] b){
        byte[] a = new byte[4];
        int i = a.length - 1,j = b.length - 1;
        for (; i >= 0 ; i--,j--) {//从b的尾部(即int值的低位)开始copy数据
            if(j >= 0)
                a[i] = b[j];
            else
                a[i] = 0;//如果b.length不足4,则将高位补0
        }
        int v0 = (a[0] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v1 = (a[1] & 0xff) << 16;
        int v2 = (a[2] & 0xff) << 8;
        int v3 = (a[3] & 0xff) ;
        return v0 + v1 + v2 + v3;
    }

    public static byte[] intToByteArray(final int integer) {
        int byteNum = (40 - Integer.numberOfLeadingZeros (integer < 0 ? ~integer : integer))/ 8;
        byte[] byteArray = new byte[4];
        for (int n = 0; n < byteNum; n++)
            byteArray[3 - n] = (byte) (integer>>> (n * 8));

        return (byteArray);
    }
}
