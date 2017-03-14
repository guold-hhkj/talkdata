//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hhkj.talkdata.api.base;

public class StatsException extends Exception {
    private static final long serialVersionUID = 1L;
    private String errorCode = "-1";
    public static final String EXCEPTION = "-99";
    public static final String INVALID_ACTION = "-111";
    public static final String HEADER_ISNULL = "-100";
    public static final String ACTION_ISNULL = "-101";
    public static final String UUID_ISNULL = "-102";
    public static final String APPKEY_ISNULL = "-103";

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public StatsException(String message) {
        super(message);
    }

    public StatsException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public StatsException(int errorCode, String message) {
        super(message);
        this.errorCode = String.valueOf(errorCode);
    }
}
