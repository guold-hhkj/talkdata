//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hhkj.talkdata.api.base;


public class PageUtil {
    private int beginIndex = 0;
    private int endIndex = 0;

    public PageUtil(Page page, int totalCount) {
        long pageTotal = (long)totalCount;
        int pageCount = getPageCount(pageTotal, page.getSize().intValue());
        page.setTotal(Long.valueOf(pageTotal));
        page.setCount(Integer.valueOf(pageCount));
        this.beginIndex = (page.getIndex().intValue() - 1) * page.getSize().intValue();
        if(this.beginIndex >= totalCount) {
            this.beginIndex = 0;
        }

        this.endIndex = this.beginIndex + page.getSize().intValue();
        if(this.endIndex >= totalCount) {
            this.endIndex = totalCount - 1;
        }

    }

    public static int getPageCount(long pageTotal, int pageSize) {
        return (int)(pageTotal / (long)pageSize) + (pageTotal % (long)pageSize > 0L?1:0);
    }

    public int getBeginIndex() {
        return this.beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getEndIndex() {
        return this.endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
}
