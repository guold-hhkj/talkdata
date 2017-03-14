package com.hhkj.talkdata.permission;

/**
 * 权限监听器
 * Created by litj on 2016/7/26.
 */
public interface OnPermissionListener {

    void onGranted();

    void onDenied();

}
