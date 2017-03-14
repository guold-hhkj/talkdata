package com.hhkj.talkdata.crash;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hhkj.talkdata.constant.CommonConstants;
import com.hhkj.talkdata.TalkDataAgent;
import com.hhkj.talkdata.bean.CrashInfoBean;
import com.hhkj.talkdata.util.SpUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 异常奔溃捕获检测并保存错误日志到本地
 * Created by litj on 2016/11/21.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private final String TAG = "CrashHandler";

    private Context context;

    private Thread.UncaughtExceptionHandler defaultHandler;

    private static CrashHandler crashHandler;

    public static CrashHandler getInstance(){
        if(crashHandler == null){
            crashHandler = new CrashHandler();
        }
        return crashHandler;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        // 获取系统默认的UncaughtException处理器
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // 保存日志文件
        saveCrashInfo(e);
        defaultHandler.uncaughtException(t, e);
        Log.e(TAG, e.getMessage());
    }

    /**
     * 保存错误信息到文件中
     */
    private void saveCrashInfo(Throwable e) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        long time = System.currentTimeMillis();
        CrashInfoBean crashInfoBean = new CrashInfoBean();
        crashInfoBean.setTime(time);
        crashInfoBean.setCrashType(e.getClass().getName());
        crashInfoBean.setCrashStackInfo(result);
        SpUtil.saveSharedPreferences(context, CommonConstants.SP_CRASH_INFO_KEY, new Gson().toJson(crashInfoBean), CommonConstants.SP_STATS);
        TalkDataAgent.getInstance().saveDataWhenExist();
    }

}
