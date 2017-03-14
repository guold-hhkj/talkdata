package com.hh.android.hhstats;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.hhkj.talkdata.TalkDataAgent;

/**
 * 测试埋点和事件流程
 * Created by litj on 2017/2/20.
 */

public class Test4Activity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_4_layout);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_event_one:
                TalkDataAgent.getInstance().onEvent("事件埋点1");
                break;
            case R.id.btn_event_two:
                TalkDataAgent.getInstance().onEvent("事件埋点2");
                break;
            case R.id.btn_event_node_one:
                TalkDataAgent.getInstance().onFlowStart("事件流程1");
                break;
            case R.id.btn_event_node_two:
                TalkDataAgent.getInstance().onFlow("事件流程2");
                break;
            case R.id.btn_event_node_three:
                TalkDataAgent.getInstance().onFlow("事件流程3");
                break;
            case R.id.btn_event_node_four:
                TalkDataAgent.getInstance().onFlowEnd("事件流程4");
                break;
            default:
                break;
        }
    }

}
