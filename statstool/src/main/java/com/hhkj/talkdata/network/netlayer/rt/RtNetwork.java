package com.hhkj.talkdata.network.netlayer.rt;

import android.util.Log;

import com.google.gson.Gson;
import com.hhkj.talkdata.api.base.RequestMessage;
import com.hhkj.talkdata.network.netlayer.base.common.NetError;
import com.hhkj.talkdata.network.netlayer.base.http.HttpConfig;
import com.hhkj.talkdata.network.netlayer.base.http.HttpImpl;
import com.hhkj.talkdata.network.netlayer.base.http.HttpTask;
import com.hhkj.talkdata.network.netlayer.base.socket.ConnImpl;
import com.hhkj.talkdata.network.netlayer.base.socket.Msg;
import com.hhkj.talkdata.network.netlayer.base.socket.MsgTask;
import com.hhkj.talkdata.network.netlayer.base.socket.OnConnectionListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by guold .
 * Date: 2015/11/25
 * Desc:
 * 同步http请求：syncHttpReq
 * 异步http请求：httpReq
 * 从缓存中请求：reqFromHttpTaskCache
 */
public class RtNetwork {

    /**
     * 异步网络请求
     *
     * @param url            请求路径
     * @param requestMessage 请求参数
     * @param doCacheFlag    是否进行缓存过滤，传true表示进行缓存过滤，缓存层会根据过滤结果决定是否进行某个接口的缓存
     * @param isGzip         是否压缩编码
     * @param callBack       提供给上层的回调
     */
    public static void httpReq(final String url, final RequestMessage requestMessage, final boolean doCacheFlag, final boolean isGzip, final RtHttpTaskUiCallBack callBack) {
        ExecuterSvr.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                final HttpTask task = new HttpTask(url, new Gson().toJson(requestMessage));
                task.setStatusListener(new HttpTask.StatusListener() {
                    @Override
                    public void onSendProgressChange(int percent) {

                    }

                    @Override
                    public void onReceiveProgressChange(int percent) {
                        if (callBack != null) {
                            try {
                                callBack.onProgress(percent, requestMessage);
                            } catch (Exception e) {
                                Log.d(this.getClass().getName(),e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onSuccess() {
                        if (doCacheFlag) {
                            //HttpTaskCache.getInstance().doFilter(task, requestMessage);
                        }
                        if (callBack != null) {
                            try {
                                callBack.onSuccess(task.getRspParam(), requestMessage);
                            } catch (Exception e) {
                                try {
                                    callBack.onError(HttpConfig.HTTP_HTTP_CALLBACK_NET_ERROR.getMsg(), requestMessage);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(NetError err) {
                        RtHttpErrorFilter.doFilter(err);
                        if (callBack != null) {
                            try {
                                callBack.onError(err.getCodeAndMsg(), requestMessage);
                            } catch (Exception e) {
                                Log.d(this.getClass().getName(),e.getMessage());
                            }
                        }
                    }
                });
                HttpImpl.getInstance().req(task, isGzip);
            }
        });
    }

    /**
     * 同步请求
     *
     * @param url            请求路径
     * @param requestMessage 请求参数
     * @param isGzip         是否压缩编码
     * @return
     */
    public static String syncHttpReq(String url, final RequestMessage requestMessage, boolean isGzip) {
        final HttpTask task = new HttpTask(url, new Gson().toJson(requestMessage));
        task.setStatusListener(new HttpTask.StatusListener() {
            @Override
            public void onSendProgressChange(int percent) {

            }

            @Override
            public void onReceiveProgressChange(int percent) {

            }

            @Override
            public void onSuccess() {
                synchronized (task) {
                    task.notify();
                }
            }

            @Override
            public void onError(NetError err) {
                task.setRspParam(err.getCodeAndMsg());
                synchronized (task) {
                    task.notify();
                }
            }
        });
        HttpImpl.getInstance().req(task, isGzip);

        synchronized (task) {
            try {
                task.wait();
            } catch (InterruptedException e) {
                Log.d(RtNetwork.class.getName(),e.getMessage());
            }
        }

        return task.getRspParam();
    }


    /**
     * 长链接管理，统计有几个长链接在保持着
     */
    private static Map<String,ConnImpl> connMap = new HashMap<String,ConnImpl>();

    /**
     * 监听到米格地址的长链接，其实就是初始化
     * @param url
     * @param defaultCallback 所有没有指定callback的响应都会进入这个callback
     */
    public static void listen(final String url, final RtMsgTaskUiCallBack defaultCallback, final OnConnectionListener connectionListener){
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (RtNetwork.class){
                    ConnImpl conn = null;
                    if(connMap.containsKey(url)){
                        conn = (ConnImpl)connMap.get(url);
                    }
                    if(conn == null){
                        MsgTask.StatusListener tmp = new MsgTask.StatusListener(){
                            @Override
                            public void onSendProgressChange(int percent) {

                            }

                            @Override
                            public void onReceiveProgressChange(int percent) {

                            }

                            @Override
                            public void onSuccess(Msg rsp, Msg req) {
                                if(defaultCallback!=null){
                                    String data = (rsp!=null&&rsp.getMsgBody()!=null)?(rsp.getMsgBody().toString()):(null);
                                    defaultCallback.onSuccess(data,req);
                                }
                            }

                            @Override
                            public void onError(NetError err,Msg msg) {
                                if(defaultCallback!=null){
                                    defaultCallback.onError(err,msg);
                                }
                            }
                        };
                        conn = ConnImpl.newInstance(url, tmp, connectionListener);
                        connMap.put(url,conn);
                    }
                }
            }
        });
        thread.start();
    }
}
