package com.njbandou.web.dto.modify;

import com.njbandou.web.dto.search.BaseSearchDTO;
import lombok.Data;

@Data
public class MenuDTO{

    private Integer pkId;

    private Integer parentId;

    private String name;

    private String title;

    private Integer orderNum;

    @Override
    public String toString() {
        return "MenuDTO{" +
                "pkId=" + pkId +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", orderNum=" + orderNum +
                '}';
    }
}

