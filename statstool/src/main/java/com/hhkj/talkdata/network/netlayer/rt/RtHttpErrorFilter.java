package com.hhkj.talkdata.network.netlayer.rt;


import com.hhkj.talkdata.network.netlayer.base.common.NetError;
import com.hhkj.talkdata.network.netlayer.base.http.HttpConfig;

/**
 * Created by guold .
 * Date: 2015/11/25
 * Desc:
 * Http底层错误过滤，过滤出简单的错误信息给上层
 */
public class RtHttpErrorFilter {
    public static boolean doFilter(NetError err){
        if(err.getCode() == HttpConfig.HTTP_URL_ERROR_CODE
                || err.getCode() == HttpConfig.HTTP_INIT_ERROR_CODE
                || err.getCode() == HttpConfig.HTTP_CONN_CONFIG_ERROR_CODE
                || err.getCode() == HttpConfig.HTTP_SEND_PARAM_ERROR_CODE
                || err.getCode() == HttpConfig.HTTP_SOCKET_IO_ERROR_CODE
                || err.getCode() == HttpConfig.HTTP_SVR_RSP_HEAD_ERROR_CODE
                || err.getCode() == HttpConfig.HTTP_SVR_RTN_CODE_ERROR_CODE
                || err.getCode() == HttpConfig.HTTP_GET_INPUT_STREAM_ERROR_CODE
                || err.getCode() == HttpConfig.HTTP_CONVERT_ZIP_ERROR_CODE
                || err.getCode() == HttpConfig.HTTP_READ_INPUT_ERROR_CODE
                || err.getCode() == HttpConfig.HTTP_TO_STRING_ERROR_CODE){
            err.setCode(HttpConfig.HTTP_DEFAULT_NET_ERROR.getCode());
            err.setMsg(HttpConfig.HTTP_DEFAULT_NET_ERROR.getMsg());
            return true;
        }
        return false;
    }
}
