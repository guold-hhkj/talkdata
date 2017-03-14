package com.hhkj.talkdata.network.netlayer.base.http;

import com.hhkj.talkdata.network.netlayer.base.common.NetError;

/**
 * Created by guold .
 * Date: 2015/11/24
 * Desc: fill it
 */
public class HttpConfig {
    //-------------------------------工作线程配置----------------------------------
    /**
     * 线程池维护线程的最少数量
     */
    public static final int COUNT_HTTP_THREAD = 3;
    /**
     * 线程池维护线程的最大数量
     */
    public static final int MAX_COUNT_HTTP_THREAD = 8;
    /**
     * 线程池维护线程所允许的空闲时间 单位:s
     */
    public static final long KEEP_ALIVE_TIME = 60;
    /**
     * 线程池所使用的缓冲队列
     */
    public static final int WORK_QUEUE_CONUT = 20;

    //-------------------------------http配置----------------------------------
    /**
     * 连接超时时间
     */
    public static final int CONNECT_TIMEOUT = 10 * 1000;

    //-------------------------------http错误配置----------------------------------
    public static final int HTTP_DEFAULT_ERROR_CODE = 0;
    public static final int HTTP_URL_ERROR_CODE = -1;
    public static final int HTTP_INIT_ERROR_CODE = -2;
    public static final int HTTP_CONN_CONFIG_ERROR_CODE = -3;
    public static final int HTTP_SEND_PARAM_ERROR_CODE = -4;
    public static final int HTTP_CANT_FIND_SVR_ERROR_CODE = -5;
    public static final int HTTP_TIME_OUT_ERROR_CODE = -6;
    public static final int HTTP_SOCKET_IO_ERROR_CODE = -7;
    public static final int HTTP_EXCEPTION_ERROR_CODE = -8;
    public static final int HTTP_SVR_RSP_HEAD_ERROR_CODE = -9;
    public static final int HTTP_SVR_RTN_CODE_ERROR_CODE = -10;
    public static final int HTTP_GET_INPUT_STREAM_ERROR_CODE = -11;
    public static final int HTTP_CONVERT_ZIP_ERROR_CODE = -12;
    public static final int HTTP_READ_INPUT_ERROR_CODE = -13;
    public static final int HTTP_TO_STRING_ERROR_CODE = -14;
    public static final int HTTP_CALLBACK_ERROR_CODE = -99;

    public static final NetError HTTP_DEFAULT_NET_ERROR = new NetError(HTTP_DEFAULT_ERROR_CODE,"网络错误！");
    public static final NetError HTTP_URL_NET_ERROR = new NetError(HTTP_URL_ERROR_CODE,"地址解析失败！");
    public static final NetError HTTP_INIT_NET_ERROR = new NetError(HTTP_INIT_ERROR_CODE,"网络初始化失败！");
    public static final NetError HTTP_CONN_CONFIG_NET_ERROR = new NetError(HTTP_CONN_CONFIG_ERROR_CODE,"链接参数配置失败！");
    public static final NetError HTTP_SEND_PARAM_NET_ERROR = new NetError(HTTP_SEND_PARAM_ERROR_CODE,"发送协议参数出错！");
    public static final NetError HTTP_CANT_FIND_SVR_NET_ERROR = new NetError(HTTP_CANT_FIND_SVR_ERROR_CODE,"找不到服务器！");
    public static final NetError HTTP_TIME_OUT_NET_ERROR = new NetError(HTTP_TIME_OUT_ERROR_CODE,"链接超时！");
    public static final NetError HTTP_SOCKET_IO_NET_ERROR = new NetError(HTTP_SOCKET_IO_ERROR_CODE,"Socket端口IO错误！");
    public static final NetError HTTP_EXCEPTION_NET_ERROR = new NetError(HTTP_EXCEPTION_ERROR_CODE,"链接异常！");
    public static final NetError HTTP_SVR_RSP_HEAD_NET_ERROR = new NetError(HTTP_SVR_RSP_HEAD_ERROR_CODE,"HTTP返回头解码出错！");
    public static final NetError HTTP_SVR_RTN_CODE_NET_ERROR = new NetError(HTTP_SVR_RTN_CODE_ERROR_CODE,"不可用返回码！");
    public static final NetError HTTP_GET_INPUT_STREAM_NET_ERROR = new NetError(HTTP_GET_INPUT_STREAM_ERROR_CODE,"得到输入流出错！");
    public static final NetError HTTP_CONVERT_ZIP_NET_ERROR = new NetError(HTTP_CONVERT_ZIP_ERROR_CODE,"转码ZIP输入流出错！");
    public static final NetError HTTP_READ_INPUT_NET_ERROR = new NetError(HTTP_READ_INPUT_ERROR_CODE,"读取输入流出错！");
    public static final NetError HTTP_TO_STRING_NET_ERROR = new NetError(HTTP_TO_STRING_ERROR_CODE,"解码字符串失败！");
    public static final NetError HTTP_HTTP_CALLBACK_NET_ERROR = new NetError(HTTP_CALLBACK_ERROR_CODE,"解码错误！");
}
