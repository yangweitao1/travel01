package com.itheima.travel.domain;

import java.util.List;

/**
 * 包名:com.itheima.travel.domain
 * 作者:Leevi
 * 日期2019-07-18  11:08
 */
public class PageBean<T> {
    private Integer currentPage;//当前页数
    private Integer pageSize;//每页的数据条数
    private Integer totalSize;//总数据条数
    private Integer totalPage;//总页数
    private List<T> list;//当前页的路线集合

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
        //设置总数据条数
        //如果总数据条数以及每页的数据条数都固定了，那么总页数也固定
        this.totalPage = totalSize % pageSize == 0 ? totalSize / pageSize : totalSize / pageSize + 1;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
