package com.hh.android.hhstats;

import android.app.Application;

import com.hhkj.talkdata.TalkDataAgent;

/**
 * Created by litj on 2017/1/13.
 */

public class StatsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //TalkDataAgent.getInstance().setChannelInZip();
        TalkDataAgent.getInstance().init(this,null);
    }
}
