package com.njbandou.web.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    private List<T> list;

    private Long totalCount;

    public PageResult(List<T> list, Long totalCount) {
        this.list = list;
        this.totalCount = totalCount;
    }
}
