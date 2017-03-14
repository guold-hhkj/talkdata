package com.hh.android.hhstats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.hhkj.talkdata.TalkDataAgent;
import com.hhkj.talkdata.constant.TradeConstants;
import com.hhkj.talkdata.hybrid.TalkDataJavaScriptInterface;

public class MainActivity extends AppCompatActivity {

    WebView wvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TalkDataAgent.getInstance().launch(this);
        setContentView(R.layout.activity_main);
        wvTest = (WebView) findViewById(R.id.wv_test);
        wvTest.getSettings().setJavaScriptEnabled(true); ///------- 设置javascript 可用
        TalkDataJavaScriptInterface talkDataJavaScriptInterface = new TalkDataJavaScriptInterface(); ////------
        wvTest.addJavascriptInterface(talkDataJavaScriptInterface, "TalkDataAgent"); // 设置js接口  第一个参数事件接口实例，第二个是实例在js中的别名，这个在js中会用到
        wvTest.loadUrl("file:///android_asset/index.html");
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Test1Activity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Test2Activity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Test3Activity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TalkDataAgent.getInstance().sendTradeInfo(TradeConstants.TRADE_BUY,
                                                          "100",
                                                          "167301",
                                                          "方正富邦保险",
                                                          "方正富邦基金管理有限公司");
            }
        });
        findViewById(R.id.btn5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = null;
                s.toString();
            }
        });
        findViewById(R.id.btn6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Test4Activity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TalkDataAgent.getInstance().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TalkDataAgent.getInstance().onPause();
    }
}
