package com.hhkj.talkdata.network.netlayer.base.common;

import com.hhkj.talkdata.network.netlayer.base.http.HttpConfig;

/**
 * Created by guold .
 * Date: 2015/11/24
 * Desc: fill it
 */
public class NetError {
    private int code;
    private String msg;
    public NetError(int code, String msg){
        setCode(code);
        setMsg(msg);
    }
    private NetError(){

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCodeAndMsg(){
        if(code == HttpConfig.HTTP_DEFAULT_ERROR_CODE){
            return msg;
        }
        return msg+"("+code+")";
    }
}
