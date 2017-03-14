//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hhkj.talkdata.api.base;

public class Page {
    private Integer index;
    private Integer size;
    private Integer count;
    private Long total;

    public Page() {
        this.index = Integer.valueOf(1);
        this.size = Integer.valueOf(20);
    }

    public Page(int index, int size) {
        if(index > 0) {
            this.index = Integer.valueOf(index);
        } else {
            this.index = Integer.valueOf(1);
        }

        if(size > 0 && size <= 200) {
            this.size = Integer.valueOf(size);
        } else {
            this.size = Integer.valueOf(20);
        }

    }

    public Page(String index, String size) {
        this(Integer.parseInt(index), Integer.parseInt(size));
    }

    public Integer getIndex() {
        return this.index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
