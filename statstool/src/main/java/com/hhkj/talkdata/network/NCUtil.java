package com.hhkj.talkdata.network;

import com.hhkj.talkdata.TalkDataAgent;
import com.hhkj.talkdata.api.base.Header;
import com.hhkj.talkdata.api.base.RequestMessage;
import com.hhkj.talkdata.api.base.ResponseMessage;
import com.hhkj.talkdata.api.base.StatsErrorCode;
import com.hhkj.talkdata.util.DateUtil;


/**
 * 请求的工具类
 * Created by litj on 2017/1/13.
 */
public class NCUtil {

    /**
     * 初始化请求头信息
     */
    public static void initHeader(RequestMessage requestMessage) {
        // 避免破坏可能带有分页的header
        if(requestMessage.getHeader() == null) {
            requestMessage.createHeader();
        }
        Header header = requestMessage.getHeader();
        header.setDevicetype(NCConfig.DEVICE_TYPE);
        header.setSendingtime(DateUtil.getNowTime());
        header.setAppkey(TalkDataAgent.getInstance().getAppKey());
        header.setChannelName(TalkDataAgent.getInstance().getChannel());
        header.setUuid(TalkDataAgent.getInstance().getDeviceId());
        header.setVersion(TalkDataAgent.getInstance().getVersionName());
        header.setDeviceModel(TalkDataAgent.getInstance().getDeviceModel());
        header.setSystemVersion(TalkDataAgent.getInstance().getSystemVersion());
        header.setLatitude(TalkDataAgent.getInstance().getLatitude());
        header.setLongitude(TalkDataAgent.getInstance().getLongitude());
        requestMessage.setHeader(header);
    }

    public static String getUrlByRequest(RequestMessage requestMessage) {
        return NCConfig.ADDRESS;
    }

    /**
     * 判断一个返回消息是否正确结果
     */
    public static boolean isCodeOK(ResponseMessage responseMessage) {
        return responseMessage != null && StatsErrorCode.OK.equals(responseMessage.getHeader().getCode());
    }

    /**
     * 从返回消息体，得到返回信息，一般用在去错误信息
     */
    public static String getMsgCode(ResponseMessage responseMessage) {
        if (responseMessage != null) {
            return responseMessage.getHeader().getAction() + ":" +
                    responseMessage.getHeader().getMsg() + "(" + responseMessage.getHeader().getCode() + ")";
        }
        return null;
    }

}
