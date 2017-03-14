package com.hhkj.talkdata.network;

import android.util.Log;

import com.google.gson.Gson;
import com.hhkj.talkdata.api.base.RequestMessage;
import com.hhkj.talkdata.api.base.ResponseMessage;
import com.hhkj.talkdata.network.netlayer.rt.RtHttpTaskUiCallBack;
import com.hhkj.talkdata.network.netlayer.rt.RtNetwork;

/**
 * 请求后数据处理接口
 *
 * @author user
 */
public class NetworkClient {
    private static NetworkClient networkClient = null;

    private NetworkClient() {

    }

    public static synchronized NetworkClient getInstance() {
        if (networkClient == null) {
            networkClient = new NetworkClient();
        }
        return networkClient;
    }

    /**
     * 异步请求，通过回掉给出结果
     * @param requestMessage
     * @param callBack
     */
    public static void request(final RequestMessage requestMessage, final NCCallBack callBack) {
        String url = NCUtil.getUrlByRequest(requestMessage);
        if (url == null) {
            if (callBack != null) {
                callBack.onError("NCError 000", requestMessage);
            }
            return;
        }
        RtNetwork.httpReq(url, requestMessage, true, true, new RtHttpTaskUiCallBack() {
            @Override
            public void onSuccess(String data, RequestMessage tag) {
                if (callBack != null) {
                    String respStr = data;
                    try {
                        ResponseMessage responseMessage = new Gson().fromJson(respStr, ResponseMessage.class);
                        if (NCUtil.isCodeOK(responseMessage)) {
                            callBack.onData(respStr, requestMessage);
                        } else {
                            callBack.onError(NCUtil.getMsgCode(responseMessage), requestMessage);
                        }
                    } catch (Exception e) {
                        Log.d(this.getClass().getName(),e.getMessage());
                    }
                }
            }

            @Override
            public void onError(String msg, RequestMessage tag) {
                if (callBack != null) {
                    try {
                        callBack.onError(msg, requestMessage);
                    } catch (Exception e) {
                        Log.d(this.getClass().getName(),e.getMessage());
                    }
                }
            }

            @Override
            public void onProgress(int percent, RequestMessage tag) {
                if (callBack != null) {
                    try {
                        callBack.onProgress(percent, requestMessage);
                    } catch (Exception e) {
                        Log.d(this.getClass().getName(),e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 同步请求，直接等待“请求-相应”完成后再返回给上层
     * @param requestMessage
     * @return
     */
    public static String request(final RequestMessage requestMessage) {
        String url = NCUtil.getUrlByRequest(requestMessage);
        if (url == null) {
            return "";
        }
        String data = RtNetwork.syncHttpReq(url, requestMessage, true);
        return data;
    }

}