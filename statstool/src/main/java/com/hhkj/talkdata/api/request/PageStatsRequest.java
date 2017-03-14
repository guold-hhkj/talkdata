//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hhkj.talkdata.api.request;


import com.hhkj.talkdata.api.base.RequestMessage;
import com.hhkj.talkdata.api.base.StatsAction;

import java.util.Map;

public class PageStatsRequest extends RequestMessage {
    /**
     * 页面名称
     */
    private String pageName;

    /**
     * 浏览次数
     */
    private String viewCount;

    /**
     * 是否是退出页面（0：否；1：是）
     */
    private String isExit;

    private Map<String, String> pageMap;

    @Override
    public void createHeader() {
        super.createHeader();
        header.setAction(StatsAction.S_PAGE_STATS);
    }

    public Map<String, String> getPageMap() {
        return pageMap;
    }

    public void setPageMap(Map<String, String> pageMap) {
        this.pageMap = pageMap;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getIsExit() {
        return isExit;
    }

    public void setIsExit(String isExit) {
        this.isExit = isExit;
    }

}
