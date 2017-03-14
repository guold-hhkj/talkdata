package com.hhkj.talkdata.network;


import com.hhkj.talkdata.api.base.RequestMessage;

/**
 * Created by guold on 2016/3/9.
 */
public interface NCCallBack {
    public void onData(String data, RequestMessage requestMessage);

    public void onProgress(int bytes, RequestMessage requestMessage);

    public void onError(String msg, RequestMessage requestMessage);
}
