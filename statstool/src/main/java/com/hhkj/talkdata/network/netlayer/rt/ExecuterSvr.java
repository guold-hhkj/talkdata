package com.hhkj.talkdata.network.netlayer.rt;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;


/**
 * Created by guold on 2016/3/7.
 */
public class ExecuterSvr {
    private static ExecuterSvr executerSvr = null;
    private Handler handler = null;
    private Thread thread = null;
    private ExecuterSvr(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    handler = new Handler(Looper.myLooper());
                    synchronized (thread){
                        thread.notify();
                    }
                    Looper.loop();
                } catch (Exception e) {
                    Log.d(this.getClass().getName(),e.getMessage());
                }
            }
        });
        thread.start();
        synchronized (thread){
            try {
                thread.wait();
            } catch (InterruptedException e) {
                Log.d(this.getClass().getName(),e.getMessage());
            }
        }
        while (true){
            if(thread != null && thread.isAlive()){
                break;
            }
        }
    }
    public static synchronized ExecuterSvr getInstance(){
        if (executerSvr == null){
            executerSvr = new ExecuterSvr();
        }
        return executerSvr;
    }

    public void execute(final Runnable runnable){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    Log.d(this.getClass().getName(),e.getMessage());
                }
            }
        });
    }
}
