package org.humor.zxc.library.commons.util.dto;

import java.io.Serializable;
import java.util.List;

public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 2063384942872471139L;
    /**
     * 当前页
     */
    private int pageNum = 1;
    /**
     * 每页的数量
     */
    private int pageSize = 10;

    /**
     * 总记录数
     */
    private long total;
    /**
     * 总页数
     */
    private int pages;

    private List<T> list;

    public PageResult() {
    }

    public PageResult(List<T> list) {
        this.list = list;
    }

    public PageResult(long total, int pageNum, int pageSize, List<T> list) {
        this.list = list;
        init(total, pageNum, pageSize);
    }

    private void init(long total, int pageNum, int pageSize) {
        //设置基本参数
        this.total = total;
        this.pageSize = pageSize;
        this.pages = (int) (this.total - 1) / this.pageSize + 1;

        //根据输入可能错误的当前号码进行自动纠正
        if (pageNum < 1) {
            this.pageNum = 1;
        } else {
            this.pageNum = Math.min(pageNum, this.pages);
        }

    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
