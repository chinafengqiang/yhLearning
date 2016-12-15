package com.yh.vo;

/**
 * Created by FQ.CHINA on 2016/11/24.
 */

public class PageInfo {
    private int pagesize = 5;
    private int offset = 0;
    private int totals = 0;

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

    public void nextOffset(){
        int nextOffset = this.offset + this.pagesize;
        if(nextOffset > totals){
            nextOffset = totals;
        }
        this.offset = nextOffset;
    }
}
