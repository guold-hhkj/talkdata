package com.hhkj.talkdata.network.netlayer.base.socket;


import com.hhkj.talkdata.network.netlayer.base.common.NetError;

/**
 * Created by guold on 2016/7/4.
 */
public class MsgTask {
    // 本次任务的请求消息
    Msg req;
    // 本次任务的请求消息对应的响应
    Msg rsp;
    // 任务状态监听
    StatusListener statusListener;
    // 任务提交的时间
    long taskSubmitTime;

    public MsgTask(Msg req, Msg rsp, StatusListener statusListener) {
        this.req = req;
        this.rsp = rsp;
        this.statusListener = statusListener;
        taskSubmitTime = System.currentTimeMillis();
    }

    public MsgTask() {
        taskSubmitTime = System.currentTimeMillis();
    }

    public long getTaskSubmitTime() {
        return taskSubmitTime;
    }

    public Msg getReq() {
        return req;
    }

    public void setReq(Msg req) {
        this.req = req;
    }

    public Msg getRsp() {
        return rsp;
    }

    public void setRsp(Msg rsp) {
        this.rsp = rsp;
    }

    public StatusListener getStatusListener() {
        return statusListener;
    }

    public void setStatusListener(StatusListener statusListener) {
        this.statusListener = statusListener;
    }

    public interface StatusListener {
        // 一下函数调用顺序为
        // 1.onSendProgressChange(0)~onSendProgressChange(100)
        // 2.onReceiveProgressChange(0)~onReceiveProgressChange(100)
        // 3.onSuccess
        // 任意时刻出错调用onError

        //  发送进度改变
        // percent 0~100   0开始，100结束；
        void onSendProgressChange(int percent);

        // 接收进度改变
        // percent 0~100   0开始，100结束
        void onReceiveProgressChange(int percent);

        // 任务完成标志,首发合一的任务两个参数都有值。只推送的消息，只有rsp有值
        void onSuccess(Msg rsp, Msg req);

        // 任务执行过程中出错，格式为“msg(code)”; 出错关联的消息，可能为空
        void onError(NetError err, Msg msg);
    }
}
