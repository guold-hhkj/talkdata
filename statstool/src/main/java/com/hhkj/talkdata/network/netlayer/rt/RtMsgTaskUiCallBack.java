package com.hhkj.talkdata.network.netlayer.rt;

import com.hhkj.talkdata.network.netlayer.base.common.NetError;

/**
 * Created by guold .
 * Date: 2015/11/25
 * Desc:
 * 成功onSuccess
 * 出错onError
 * 进度变化onProgress
 */
public interface RtMsgTaskUiCallBack {
    /**
     * 网络请求成功
     * @param rsp 返回参数字符串
     * @param req 返回对应的请求
     */
    public void onSuccess(String rsp, Object req);

    /**
     * 网络请求发生错误
     * @param err 错误信息
     * @param req 错误对应的请求号
     */
    public void onError(NetError err, Object req);

    /**
     * 进度改变
     * @param percent 进度百分值
     * @param req 对应的请求
     */
    public void onProgress(int percent, Object req);
}
