package com.hhkj.talkdata.network.netlayer.base.http;

import android.os.Parcel;
import android.os.Parcelable;


import com.hhkj.talkdata.network.netlayer.base.common.NetError;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by guold .
 * Date: 2015/11/24
 * Desc: fill it
 */
public class HttpTask implements Parcelable, Serializable {
    private String reqUrl;     // 请求地址
    private String reqParam;   // 请求参数
    private long reqTime;    // 请求发起时间
    private long reqSOTime;  // 请求发送成功时间 SO == send over

    private String rspParam;     // 返回消息
    private long rspTime;    // 返回开始接收时间
    private long rspROTime;  // 请求接收完毕时间 RO == receive time

    private StatusListener statusListener; // 进度监听


    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(reqUrl);
        out.writeObject(reqParam);
        out.writeLong(reqTime);
        out.writeLong(reqSOTime);

        out.writeObject(rspParam);
        out.writeLong(rspTime);
        out.writeLong(rspROTime);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        reqUrl = (String)in.readObject();
        reqParam = (String)in.readObject();
        reqTime = in.readLong();
        reqSOTime = in.readLong();

        rspParam = (String)in.readObject();
        rspTime = in.readLong();
        rspROTime = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getReqUrl());
        dest.writeString(getReqParam());
        dest.writeLong(getReqTime());
        dest.writeLong(getReqSOTime());

        dest.writeString(getRspParam());
        dest.writeLong(getRspTime());
        dest.writeLong(getRspROTime());
    }

    public HttpTask(String reqUrl, String reqParam){
        setReqUrl(reqUrl);
        setReqParam(reqParam);
    }

    public HttpTask(HttpTask obj){
        this.reqUrl = obj.getReqUrl();
        this.reqParam = obj.getReqParam();
        this.reqTime = obj.getReqTime();
        this.reqSOTime = obj.getReqSOTime();

        this.rspParam = obj.getRspParam();
        this.rspTime = obj.getRspTime();
        this.rspROTime = obj.getRspROTime();
        this.statusListener = obj.getStatusListener();
    }

    private HttpTask(Parcel in){
        setReqUrl(in.readString());
        setReqParam(in.readString());
        setReqTime(in.readLong());
        setReqSOTime(in.readLong());

        setRspParam(in.readString());
        setRspTime(in.readLong());
        setRspROTime(in.readLong());
    }

    public static final Creator<HttpTask> CREATOR = new Creator<HttpTask>(){
        @Override
        public HttpTask createFromParcel(Parcel source) {
            return new HttpTask(source);
        }

        @Override
        public HttpTask[] newArray(int size) {
            return new HttpTask[size];
        }
    };

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public String getReqParam() {
        return reqParam;
    }

    public void setReqParam(String reqParam) {
        this.reqParam = reqParam;
    }

    public long getReqTime() {
        return reqTime;
    }

    public void setReqTime(long reqTime) {
        this.reqTime = reqTime;
    }

    public String getRspParam() {
        return rspParam;
    }

    public void setRspParam(String rspParam) {
        this.rspParam = rspParam;
    }

    public long getRspTime() {
        return rspTime;
    }

    public void setRspTime(long rspTime) {
        this.rspTime = rspTime;
    }

    public long getReqSOTime() {
        return reqSOTime;
    }

    public void setReqSOTime(long reqSOTime) {
        this.reqSOTime = reqSOTime;
    }

    public long getRspROTime() {
        return rspROTime;
    }

    public void setRspROTime(long rspROTime) {
        this.rspROTime = rspROTime;
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

        // 任务完成标志
        // 这时，getRspParam返回的才是有效值
        void onSuccess();

        // 任务执行过程中出错，格式为“msg(code)”;
        void onError(NetError err);
    }
}
