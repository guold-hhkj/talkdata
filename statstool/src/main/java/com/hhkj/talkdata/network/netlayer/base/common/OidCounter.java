package com.hhkj.talkdata.network.netlayer.base.common;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by guold on 2016/6/30.
 */
public class OidCounter {
    private AtomicInteger num = new AtomicInteger(0);
    public static final int MAX_NUM = 16777215-1;
    public int getNum(){
        num.compareAndSet(MAX_NUM,0);
        return num.getAndIncrement();
    }
}
