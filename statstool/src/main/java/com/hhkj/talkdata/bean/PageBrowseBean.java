package com.hhkj.talkdata.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 页面浏览次数
 * Created by litj on 2017/1/13.
 */

public class PageBrowseBean implements Serializable{

    private String existPageName;

    private Map<String, Integer> pagesBrowseInfo;

    public PageBrowseBean(){
        pagesBrowseInfo = new HashMap<>();
    }

    /**
     * 某页面浏览次数+1
     */
    public void addPageBrowseCount(String pageName){
        if (pagesBrowseInfo.containsKey(pageName)){
            pagesBrowseInfo.put(pageName, pagesBrowseInfo.get(pageName) + 1);
        }
    }

    /**
     * 添加应该被统计的页面
     */
    public void addShouldStatsPage(String pageName){
        if (!pagesBrowseInfo.containsKey(pageName)){
            pagesBrowseInfo.put(pageName, 0);
        }
    }

    /**
     * 判断某页面是否需要被统计
     */
    public boolean shouldBeStats(String pageName){
        return pagesBrowseInfo.containsKey(pageName);
    }

    public String getExistPageName() {
        return existPageName;
    }

    public void setExistPageName(String existPageName) {
        // 只有当前需要统计
        this.existPageName = existPageName;
    }

    public Map<String, Integer> getPagesBrowseInfo() {
        return pagesBrowseInfo;
    }

    public void reset(){
        existPageName = "";
        pagesBrowseInfo.clear();
    }

    public boolean hasData(){
        return !TextUtils.isEmpty(existPageName) || pagesBrowseInfo.size() > 0;
    }

}
