package com.hhkj.talkdata.network.netlayer.rt;


import com.hhkj.talkdata.api.base.RequestMessage;

/**
 * Created by guold .
 * Date: 2015/11/25
 * Desc:
 * 成功onSuccess
 * 出错onError
 * 进度变化onProgress
 */
public interface RtHttpTaskUiCallBack {
    /**
     * 网络请求成功
     * @param data 返回参数字符串
     * @param tag 返回对应的请求
     */
    public void onSuccess(String data, RequestMessage tag);

    /**
     * 网络请求发生错误
     * @param msg 错误信息
     * @param tag 错误对应的请求号
     */
    public void onError(String msg, RequestMessage tag);

    /**
     * 进度改变
     * @param percent 进度百分值
     * @param tag 对应的请求
     */
    public void onProgress(int percent, RequestMessage tag);
}
