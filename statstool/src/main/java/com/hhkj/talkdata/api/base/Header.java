//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hhkj.talkdata.api.base;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Header {
    public static final int REQUEST = 0;
    public static final int RESPONSE = 1;
    private String action;
    private String uuid;
    private String appkey;
    private Integer msgtype;
    private String devicetype;
    private String ip;
    private String version;
    private String sendingtime;
    private String code;
    private String msg;
    private String channelName;
    private String deviceModel;
    private String systemVersion;
    private String longitude;
    private String latitude;

    public static String getDateTime() {
        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return tempDate.format(new Date());
    }

    public Header() {
        this.code = "0";
        this.sendingtime = getDateTime();
    }

    public Header(Header header) {
        this();
        this.create(header);
    }

    public void create(Header header) {
        if(header != null) {
            this.action = header.action;
            this.uuid = header.uuid;
            this.appkey = header.appkey;
            this.version = header.version;
        }

    }

    public void init(Header header) {
        if(header != null) {
            this.action = header.action;
            this.uuid = header.uuid;
            this.appkey = header.appkey;
            this.version = header.version;
            this.devicetype = header.devicetype;
        }

    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAppkey() {
        return this.appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public Integer getMsgtype() {
        return this.msgtype;
    }

    public void setMsgtype(Integer msgtype) {
        this.msgtype = msgtype;
    }

    public String getDevicetype() {
        return this.devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSendingtime() {
        return this.sendingtime;
    }

    public void setSendingtime(String sendingtime) {
        this.sendingtime = sendingtime;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
