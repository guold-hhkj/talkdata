package com.hhkj.talkdata;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.hhkj.talkdata.bean.PageBrowseBean;
import com.hhkj.talkdata.constant.CommonConstants;
import com.hhkj.talkdata.crash.CrashHandler;
import com.hhkj.talkdata.api.TalkDataApiHelper;
import com.hhkj.talkdata.network.NCConfig;
import com.hhkj.talkdata.permission.EasyPermissions;
import com.hhkj.talkdata.permission.OnPermissionListener;
import com.hhkj.talkdata.util.AppUtil;
import com.hhkj.talkdata.util.DeviceUuidFactory;
import com.hhkj.talkdata.util.SpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * 统计管理类
 * Created by litj on 2017/1/11.
 */

public class TalkDataAgent {

    private static final String TAG = "TalkDataAgent";

    private Context context;

    private String appKey;
    /**
     * 渠道信息
     */
    private String channel;
    /**
     * 版本号
     */
    private String versionName;
    /**
     * 设备型号
     */
    private String deviceModel;
    /**
     * 系统版本
     */
    private String systemVersion;
    /**
     * 设备ID，一个设备ID对应一个设备
     */
    private String deviceId;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 本次App的启动时刻
     */
    private long startTime;
    /**
     * 程序是否进入到后台
     */
    private boolean isBackGround = false;
    /**
     * 是否在后台超过30秒
     */
    private boolean isBeyond30Seconds = false;
    /**
     * 是否刚打开应用
     */
    private boolean isOpened = true;
    /**
     * 页面浏览统计
     */
    private PageBrowseBean pageBrowseBean;
    /**
     * 当前可统计跳出率的页面名字
     */
    private String currentCanStatsExistPageName;
    /**
     * 是否渠道信息是从apk中META-INF文件夹下的空文件获取
     */
    private boolean isChannelInZip = false;

    private static TalkDataAgent instance = null;

    public void init(Context context,String svrAddr) {
        this.context = context;
        if(svrAddr!=null){
            NCConfig.ADDRESS = svrAddr;
        }
        deviceModel = android.os.Build.MODEL;
        systemVersion = android.os.Build.VERSION.RELEASE;
        appKey = AppUtil.getMetaData(context, CommonConstants.APP_KEY);
        /* 渠道信息获取分两种，一种是从APK的META_INF文件夹中获取
         * 另一种是从MetaData中获取，默认是从MetaData中获取 */
        if (isChannelInZip) {
            // TODO: 2017/1/16 apk如果过大，循环次数可能会非常多。有待优化
            channel = AppUtil.getChannelByZip(context);
        } else {
            channel = AppUtil.getMetaData(context, CommonConstants.TALK_DATA_CHANNEL);
        }
        if (TextUtils.isEmpty(channel)) {
            channel = "unknown";
        }
        versionName = AppUtil.getVersion(context);
        startTime = System.currentTimeMillis();
        pageBrowseBean = new PageBrowseBean();
        // 如果是通过ANDROID_ID无效，那么这里获取deviceID将会失败
        generateDeviceId(context);
        // 奔溃检测
        CrashHandler.getInstance().init(context);
    }

    /**
     * 在App第一个启动的Activity的onCreate方法里面调用
     */
    public void launch(final Context context) {
        generateDeviceId(context);
        final Handler handler = new Handler();
        appOpenApiCall();
        if (!EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            EasyPermissions.requestPermissions(context, "需要获得手机信息权限", 255, new OnPermissionListener() {
                @Override
                public void onGranted() {
                    if(isOpened) {
                        isOpened = false;
                        getLocation(context, handler);
                    }
                }

                @Override
                public void onDenied() {
                    if(isOpened) {
                        isOpened = false;
                        sendChannel();
                    }
                }
            }, Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            if(isOpened) {
                isOpened = false;
                getLocation(context, handler);
            }
        }
    }

    private LocationManager locationManager;

    private void getLocation(Context context, Handler handler) {
        // 获取地理位置管理器
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        String locationProvider;
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            // 如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            // 如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Log.i(TAG, "没有可用的位置提供器");
            sendChannel();
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if(location != null) {
            longitude = location.getLongitude() + "";
            latitude = location.getLatitude() + "";
            Log.i(TAG, longitude + " " + latitude);
            sendChannel();
        }
        else {
            locationManager.requestLocationUpdates(locationProvider, 3000, 0, mLocationListener);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendChannel();
                }
            }, 1000 * 5);
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateToNewLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    private void updateToNewLocation(Location location) {
        if(location != null) {
            locationManager.removeUpdates(mLocationListener);
            locationManager = null;
            longitude = location.getLongitude() + "";
            latitude = location.getLatitude() + "";
            sendChannel();
            Log.i(TAG, longitude + " " + latitude);
        }
    }

    /**
     * 设置当前渠道是从apk的META-INF的空文件中获取
     */
    public void setChannelInZip(){
        isChannelInZip = true;
    }

    /**
     * 发送渠道信息
     */
    private void sendChannel() {
        if(TextUtils.isEmpty(channel)){
            return;
        }
        String spChannel = SpUtil.getStringValue(context, CommonConstants.SP_STATS, CommonConstants.SP_CHANNEL_KEY);
        if(TextUtils.isEmpty(spChannel) || (!channel.equals(spChannel) && !TextUtils.isEmpty(spChannel))){
            /* 本地没有存储channel，说明是第一次启动App，或者本地有存储channel，但是与当前的App的channel不一致，
             * 说明是重新下载其他渠道的App并覆盖安装了 */
            TalkDataApiHelper.sendChannel(new OnChannelSendStatusListener() {
                @Override
                public void onSuccess() {
                    SpUtil.saveSharedPreferences(context, CommonConstants.SP_CHANNEL_KEY, channel, CommonConstants.SP_STATS);
                }
            });
        }
    }

    public static TalkDataAgent getInstance(){
        if (instance == null){
            instance = new TalkDataAgent();
        }
        return instance;
    }

    private void generateDeviceId(Context context){
        UUID uuid = new DeviceUuidFactory(context).getDeviceUuid();
        if(uuid != null) {
            deviceId = uuid.toString();
        }
    }

    /**
     * 调用App启动时相关接口
     */
    private void appOpenApiCall(){
        sendLastExistData();
        sendUserInfo();
    }

    /**
     * 上传上次退出App时保存的内容上传
     */
    private void sendLastExistData() {
        // 奔溃数据上传
        TalkDataApiHelper.sendCrashInfo(context);
        // 页面浏览统计数据上传
        TalkDataApiHelper.sendPageBrowseInfo(context);
    }

    /**
     * 上传启动App时的相关用户数据
     */
    private void sendUserInfo() {
        long usingTime = SpUtil.getLongValue(context, CommonConstants.SP_STATS, CommonConstants.SP_USING_TIME_KEY, -1);
        if(usingTime > 1000 * 60 * 60 * 24){
            // 容错处理，使用时长大于1天算无效
            usingTime = -1;
        }
        SpUtil.reomveValue(context, CommonConstants.SP_STATS, CommonConstants.SP_USING_TIME_KEY);
        TalkDataApiHelper.sendUserInfo(usingTime);
    }

    private Runnable saveExistDataRunnable = new Runnable() {
        @Override
        public void run() {
            if(isBackGround) {
                // 此时已经退出到后台超过30秒，算是已经退出
                isBeyond30Seconds = true;
                boolean shouldBeStats = pageBrowseBean.shouldBeStats(currentCanStatsExistPageName);
                if (shouldBeStats) {
                    // 如果这页需要统计跳出率
                    pageBrowseBean.setExistPageName(currentCanStatsExistPageName);
                }
                saveDataWhenExist();
            }
        }
    };

    /**
     * 当App退出时存储部分数据
     */
    public void saveDataWhenExist(){
        // 存储使用时长
        SpUtil.saveSharedPreferences(context, CommonConstants.SP_USING_TIME_KEY, System.currentTimeMillis() - startTime, CommonConstants.SP_STATS);
        // 存储页面浏览统计信息
        if(pageBrowseBean != null){
            String pageBrowseInfo = new Gson().toJson(pageBrowseBean);
            if(!TextUtils.isEmpty(pageBrowseInfo)){
                SpUtil.saveSharedPreferences(context, CommonConstants.SP_PAGE_BROWSE_KEY, pageBrowseInfo, CommonConstants.SP_STATS);
            }
        }
    }

    /**
     * 在Activity或Fragment的生命周期onResume里面添加，用于统计页面浏览次数和判断App是否在后台超过30秒
     */
    public void onResume(){
        if(isBackGround){
            // 说明刚从后台转到前台
            isBackGround = false;
            if(isBeyond30Seconds){
                isBeyond30Seconds = false;
                startTime = System.currentTimeMillis();
                appOpenApiCall();
                // 上传页面统计数据后还原数据
                pageBrowseBean.reset();
            }
        }
    }

    /**
     * 在Activity或Fragment的生命周期onPause里面添加，用于统计页面浏览次数和判断App是否在后台超过30秒
     */
    public void onPause(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackGround = AppUtil.isBackground(context);
                if(isBackGround){
                    handler.postDelayed(saveExistDataRunnable, 1000 * 30);
                }
            }
        }, 1000);
    }

    /**
     * 标记某页面需要统计页面跳出率
     */
    public void shouldGetPageExistData(String pageName){
        currentCanStatsExistPageName = pageName;
        pageBrowseBean.addShouldStatsPage(pageName);
        pageBrowseBean.addPageBrowseCount(pageName);
    }

    /**
     * 发送交易信息
     * @param tradeType 交易类型
     * @param tradeNum 交易金额
     * @param fundCode 基金代码
     * @param fundName 基金名称
     * @param company 基金公司
     */
    public void sendTradeInfo(String tradeType, String tradeNum, String fundCode, String fundName, String company){
        TalkDataApiHelper.sendTradeInfo(tradeType, tradeNum, fundCode, fundName, company);
    }

    /**
     * 事件埋点
     * @param eventId 事件ID
     */
    public void onEvent(String eventId){
        TalkDataApiHelper.sendEvent(eventId);
    }

    private long lastNodeTime = -1;
    private ArrayList<String> nodeList;

    /**
     * 流程开始
     * @param nodeName 节点名称
     */
    public void onFlowStart(String nodeName){
        TalkDataApiHelper.sendFlowNodeInfo(nodeName, "0");
        nodeList = new ArrayList<>();
        nodeList.add(nodeName);
        lastNodeTime = System.currentTimeMillis();
    }

    /**
     * 流程结束
     * @param nodeName 节点名称
     */
    public void onFlowEnd(String nodeName){
        if(lastNodeTime != -1) {
            TalkDataApiHelper.sendFlowNodeInfo(nodeName, (System.currentTimeMillis() - lastNodeTime) / 1000 + "");
            nodeList.clear();
            lastNodeTime = -1;
        }
    }

    /**
     * 流程中的节点
     * @param nodeName 节点名称
     */
    public void onFlow(String nodeName){
        if (nodeList == null || nodeList.contains(nodeName) || lastNodeTime == -1) {
            return;
        }
        TalkDataApiHelper.sendFlowNodeInfo(nodeName, (System.currentTimeMillis() - lastNodeTime) / 1000 + "");
        nodeList.add(nodeName);
        lastNodeTime = System.currentTimeMillis();
    }

    /**
     * 渠道发送回调接口，只有服务端接收渠道成功才会回调
     */
    public interface OnChannelSendStatusListener {
        void onSuccess();
    }

    public String getChannel(){
        return channel;
    }

    public String getAppKey(){
        return appKey;
    }

    public String getDeviceId(){
        return deviceId;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

}
