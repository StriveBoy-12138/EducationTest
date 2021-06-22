package com.njbandou.web.dto.search;

import lombok.Data;

@Data
public class BaseSearchDTO {
    private Integer page;
    private Integer pageSize;

    public Integer getPage() {
        return page * pageSize;
    }

    public void setPage(Integer page) {
        this.page = page - 1;
    }
}
