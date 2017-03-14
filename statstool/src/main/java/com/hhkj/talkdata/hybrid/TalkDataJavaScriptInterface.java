package com.hhkj.talkdata.hybrid;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.hhkj.talkdata.TalkDataAgent;

/**
 * js接口
 * Created by litj on 2017/2/7.
 */

public class TalkDataJavaScriptInterface {

    @JavascriptInterface
    public void shouldGetPageExistData(String pageName){
        Log.i("TalkDataJavaScript", "shouldGetPageExistData" + pageName);
        TalkDataAgent.getInstance().shouldGetPageExistData(pageName);
    }

    @JavascriptInterface
    public void sendTradeInfo(String tradeType, String tradeNum, String fundCode, String fundName, String company){
        Log.i("TalkDataJavaScript", "sendTradeInfo" + fundCode);
        TalkDataAgent.getInstance().sendTradeInfo(tradeType, tradeNum, fundCode, fundName, company);
    }

}
