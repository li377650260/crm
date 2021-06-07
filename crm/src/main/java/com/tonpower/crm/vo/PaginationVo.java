package com.tonpower.crm.vo;

import java.util.List;

/**
 * @description:
 * @author: li377650260
 * @date: 2021/6/3 23:52
 */
public class PaginationVo<T> {
    private int total;
    private List<T> dataList;

    public PaginationVo() {
    }

    @Override
    public String toString() {
        return "PaginationVo{" +
                "total=" + total +
                ", dataList=" + dataList +
                '}';
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
