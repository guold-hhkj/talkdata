package com.hh.android.hhstats;

import android.app.Activity;
import android.os.Bundle;

import com.hhkj.talkdata.TalkDataAgent;

/**
 * Created by litj on 2017/1/13.
 */

public class Test2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        TalkDataAgent.getInstance().onResume();
        TalkDataAgent.getInstance().shouldGetPageExistData("测试页");
    }

    @Override
    protected void onPause() {
        super.onPause();
        TalkDataAgent.getInstance().onPause();
    }
}
