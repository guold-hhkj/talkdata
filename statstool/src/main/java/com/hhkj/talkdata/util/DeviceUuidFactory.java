package com.hhkj.talkdata.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.hhkj.talkdata.permission.EasyPermissions;
import com.hhkj.talkdata.permission.OnPermissionListener;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * 设备ID生成工具
 * Created by litj on 2017/1/9.
 */

public class DeviceUuidFactory {

    private static final String PREFS_FILE = "device_id.xml";
    private static final String PREFS_DEVICE_ID = "device_id";
    private static volatile UUID uuid;

    public DeviceUuidFactory(final Context context) {
        if (uuid == null) {
            synchronized (DeviceUuidFactory.class) {
                if (uuid == null) {
                    final SharedPreferences prefs = context
                            .getSharedPreferences(PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);
                    if (id != null) {
                        // Use the ids previously computed and stored in the
                        // prefs file
                        uuid = UUID.fromString(id);
                    } else {
                        final String androidId = Settings.Secure.getString(
                                context.getContentResolver(), Settings.Secure.ANDROID_ID);
                        // Use the Android ID unless it's broken, in which case
                        // fallback on deviceId,
                        // unless it's not available, then fallback on a random
                        // number which we store to a prefs file
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId
                                        .getBytes("utf8"));
                            } else {
                                if(context instanceof Activity) {
                                    if (!EasyPermissions.hasPermissions(context, Manifest.permission.READ_PHONE_STATE)) {
                                        EasyPermissions.requestPermissions(context, "需要获得手机信息权限", 255, new OnPermissionListener() {
                                            @Override
                                            public void onGranted() {
                                                final String deviceId = ((TelephonyManager)
                                                        context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                                try {
                                                    uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onDenied() {

                                            }
                                        }, Manifest.permission.READ_PHONE_STATE);
                                    } else {
                                        final String deviceId = ((TelephonyManager)
                                                context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                        uuid = deviceId != null ? UUID
                                                .nameUUIDFromBytes(deviceId
                                                        .getBytes("utf8")) : UUID
                                                .randomUUID();
                                    }
                                }
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        // Write the value out to the prefs file
                        if(uuid != null) {
                            prefs.edit()
                                    .putString(PREFS_DEVICE_ID, uuid.toString())
                                    .apply();
                        }
                    }
                }
            }
        }
    }

    public UUID getDeviceUuid() {
        return uuid;
    }
}