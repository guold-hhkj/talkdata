package com.hhkj.talkdata.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.hhkj.talkdata.api.request.ClickStatisticsRequest;
import com.hhkj.talkdata.api.request.FlowStatsRequest;
import com.hhkj.talkdata.constant.CommonConstants;
import com.hhkj.talkdata.TalkDataAgent;
import com.hhkj.talkdata.api.base.RequestMessage;
import com.hhkj.talkdata.api.request.DeviceMesRequest;
import com.hhkj.talkdata.api.request.GetBreakdownMesRequest;
import com.hhkj.talkdata.api.request.GetTimeRequest;
import com.hhkj.talkdata.api.request.GetTradeMesRequest;
import com.hhkj.talkdata.api.request.PageStatsRequest;
import com.hhkj.talkdata.bean.CrashInfoBean;
import com.hhkj.talkdata.bean.PageBrowseBean;
import com.hhkj.talkdata.network.NCCallBack;
import com.hhkj.talkdata.network.NCUtil;
import com.hhkj.talkdata.network.NetworkClient;
import com.hhkj.talkdata.util.SpUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 崩溃信息上传帮助类
 * Created by litj on 2017/1/13.
 */

public class TalkDataApiHelper {

    public static final String TAG = "TalkDataApiHelper";
    
    /**
     * 上传奔溃信息
     */
    public static void sendCrashInfo(Context context){
        String crashInfo = SpUtil.getStringValue(context, CommonConstants.SP_STATS, CommonConstants.SP_CRASH_INFO_KEY);
        if(!TextUtils.isEmpty(crashInfo)){
            SpUtil.reomveValue(context, CommonConstants.SP_STATS, CommonConstants.SP_CRASH_INFO_KEY);
            CrashInfoBean crashInfoBean = new Gson().fromJson(crashInfo, CrashInfoBean.class);
            GetBreakdownMesRequest request = new GetBreakdownMesRequest();
            NCUtil.initHeader(request);
            request.setDate(new Date(crashInfoBean.getTime()));
            request.setName(crashInfoBean.getCrashType());
            request.setReason(crashInfoBean.getCrashStackInfo());
            NetworkClient.request(request, new NCCallBack() {
                @Override
                public void onData(String data, RequestMessage requestMessage) {
                    Log.i(TAG, "onData:" + data);
                }

                @Override
                public void onProgress(int bytes, RequestMessage requestMessage) {

                }

                @Override
                public void onError(String msg, RequestMessage requestMessage) {
                    Log.i(TAG, "onError:" + msg);
                }
            });
        }
    }

    /**
     * 打开App时上传用户相关信息
     */
    public static void sendUserInfo(long usingTime){
        GetTimeRequest request = new GetTimeRequest();
        NCUtil.initHeader(request);
        request.setTime(usingTime / 1000 + "");
        NetworkClient.request(request, new NCCallBack() {
            @Override
            public void onData(String data, RequestMessage requestMessage) {
                Log.i(TAG, "onData:" + data);
            }

            @Override
            public void onProgress(int bytes, RequestMessage requestMessage) {

            }

            @Override
            public void onError(String msg, RequestMessage requestMessage) {
                Log.i(TAG, "onData:" + msg);
            }
        });
    }

    /**
     * 打开App时上传页面浏览信息
     */
    public static void sendPageBrowseInfo(Context context){
        String pageBrowseInfo = SpUtil.getStringValue(context, CommonConstants.SP_STATS, CommonConstants.SP_PAGE_BROWSE_KEY);
        if(!TextUtils.isEmpty(pageBrowseInfo)){
            SpUtil.reomveValue(context, CommonConstants.SP_STATS, CommonConstants.SP_PAGE_BROWSE_KEY);
            PageBrowseBean pageBrowseInfoBean = new Gson().fromJson(pageBrowseInfo, PageBrowseBean.class);
            if(pageBrowseInfoBean.hasData()) {
                PageStatsRequest request = new PageStatsRequest();
                request.setPageMap(getPageBrowseMapData(pageBrowseInfoBean));
                NCUtil.initHeader(request);
                NetworkClient.request(request, new NCCallBack() {
                    @Override
                    public void onData(String data, RequestMessage requestMessage) {
                        Log.i(TAG, "onData:" + data);
                    }

                    @Override
                    public void onProgress(int bytes, RequestMessage requestMessage) {

                    }

                    @Override
                    public void onError(String msg, RequestMessage requestMessage) {
                        Log.i(TAG, "onData:" + msg);
                    }
                });
            }
        }
    }

    private static Map<String, String> getPageBrowseMapData(PageBrowseBean pageBrowseInfoBean){
        Map<String, String> result = new HashMap<>();
        for(Map.Entry<String, Integer> entry : pageBrowseInfoBean.getPagesBrowseInfo().entrySet()){
            String exist = entry.getKey().equals(pageBrowseInfoBean.getExistPageName()) ? "1" : "0";
            result.put(entry.getKey(), entry.getValue() + "," + exist);
        }
        return result;
    }

    /**
     * 发送交易信息
     * @param tradeType 交易类型
     * @param tradeNum 交易金额
     * @param fundCode 基金代码
     * @param company 基金公司
     */
    public static void sendTradeInfo(String tradeType, String tradeNum, String fundCode, String fundName, String company){
        GetTradeMesRequest request = new GetTradeMesRequest();
        NCUtil.initHeader(request);
        request.setFundCode(fundCode);
        request.setFundCompany(company);
        if("0".equals(tradeType)) {
            request.setTradeMoney(tradeNum);
        }
        else if("1".equals(tradeType)) {
            request.setTradeShare(tradeNum);
        }
        request.setFundName(fundName);
        request.setTradeType(tradeType);
        NetworkClient.request(request, new NCCallBack() {
            @Override
            public void onData(String data, RequestMessage requestMessage) {
                Log.i(TAG, "onData:" + data);
            }

            @Override
            public void onProgress(int bytes, RequestMessage requestMessage) {

            }

            @Override
            public void onError(String msg, RequestMessage requestMessage) {
                Log.i(TAG, "onData:" + msg);
            }
        });
    }

    /**
     * 上传渠道信息
     */
    public static void sendChannel(final TalkDataAgent.OnChannelSendStatusListener listener){
        DeviceMesRequest request = new DeviceMesRequest();
        NCUtil.initHeader(request);
        NetworkClient.request(request, new NCCallBack() {
            @Override
            public void onData(String data, RequestMessage requestMessage) {
                Log.i(TAG, "onData:" + data);
                listener.onSuccess();
            }

            @Override
            public void onProgress(int bytes, RequestMessage requestMessage) {

            }

            @Override
            public void onError(String msg, RequestMessage requestMessage) {
                Log.i(TAG, "onData:" + msg);
            }
        });
    }

    public static void sendEvent(String eventId){
        ClickStatisticsRequest request = new ClickStatisticsRequest();
        NCUtil.initHeader(request);
        request.setBuryPointId(eventId);
        NetworkClient.request(request, new NCCallBack() {
            @Override
            public void onData(String data, RequestMessage requestMessage) {

            }

            @Override
            public void onProgress(int bytes, RequestMessage requestMessage) {

            }

            @Override
            public void onError(String msg, RequestMessage requestMessage) {

            }
        });
    }

    /**
     * 发送事件流程信息
     * @param nodeName 流程节点名称
     * @param time 从上个流程到这个流程的耗时（单位：秒）
     */
    public static void sendFlowNodeInfo(String nodeName, String time){
        // 容错处理，如果时间太长或者时间小于0，则不发送数据
        if(!TextUtils.isEmpty(time) && (Long.valueOf(time) > 60 * 60 * 24 || Long.valueOf(time) < 0)) {
            return;
        }
        FlowStatsRequest request = new FlowStatsRequest();
        NCUtil.initHeader(request);
        request.setFlowId(nodeName);
        request.setUseLength(time);
        NetworkClient.request(request, new NCCallBack() {
            @Override
            public void onData(String data, RequestMessage requestMessage) {

            }

            @Override
            public void onProgress(int bytes, RequestMessage requestMessage) {

            }

            @Override
            public void onError(String msg, RequestMessage requestMessage) {

            }
        });
    }

}
