package com.hhkj.talkdata.network.netlayer.base.socket;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by guold on 2016/7/5.
 * 消息计数
 */
public class MsgCounter {
    private AtomicInteger num = new AtomicInteger(0);
    public static final int MAX_NUM = 16777215-1;
    public int getNum(){
        num.compareAndSet(MAX_NUM,0);
        return num.getAndIncrement();
    }
}
