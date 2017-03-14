package com.hhkj.talkdata.network.netlayer.base.socket;


import com.hhkj.talkdata.network.netlayer.base.common.NetError;

/**
 * Created by guold on 2016/7/5.
 */
public class ConnConfig {
    //-------------------------------长链接配置----------------------------------
    /**
     * 连接超时时间
     */
    public static final int CONNECT_TIMEOUT = 10 * 1000;

    //-------------------------------长链接错误配置----------------------------------
    public static final int CONN_DEFAULT_ERROR_CODE = 0;
    public static final int CONN_ONSUCCESS_ERROR_CODE = -1;
    public static final int CONN_ENCODE_ERROR_CODE = -2;
    public static final int CONN_DECODE_ERROR_CODE = -3;
    public static final int CONN_CONNECT_ERROR_CODE = -4;
    public static final int CONN_OPENINPUT_ERROR_CODE = -5;
    public static final int CONN_OPENOUTPUT_ERROR_CODE = -6;
    public static final int CONN_UNKONWN_ERROR_CODE = -99;

    public static final NetError CONN_DEFAULT_ERROR = new NetError(CONN_DEFAULT_ERROR_CODE,"网络错误！");
    public static final NetError CONN_ONSUCCESS_ERROR = new NetError(CONN_ONSUCCESS_ERROR_CODE,"onsuccess err！");
    public static final NetError CONN_ENCODE_ERROR = new NetError(CONN_ENCODE_ERROR_CODE,"发送消息时编码失败！");
    public static final NetError CONN_DECODE_ERROR = new NetError(CONN_DECODE_ERROR_CODE,"收到消息时解码失败！");
    public static final NetError CONN_CONNECT_ERROR = new NetError(CONN_CONNECT_ERROR_CODE,"链接失败！");
    public static final NetError CONN_OPRNINPUT_ERROR = new NetError(CONN_OPENINPUT_ERROR_CODE,"打开Socket输入流失败！");
    public static final NetError CONN_OPRNOUTPUT_ERROR = new NetError(CONN_OPENOUTPUT_ERROR_CODE,"打开Socket输出流失败！");
    public static final NetError CONN_UNKONWN_ERROR = new NetError(CONN_UNKONWN_ERROR_CODE,"打开Socket输出流失败！");
}
