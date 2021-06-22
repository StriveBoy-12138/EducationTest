package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.CatalogEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class CatalogDTO {
    private Integer pkId;
    @NotNull(message = "课程id不能为空")
    private Integer classId;
    private String title;
    private Integer sort;
    private String move;

    private CatalogEntity set(CatalogEntity catalog){
        catalog.setTitle(title);
        catalog.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return catalog;
    }

    public CatalogEntity insert(){
        CatalogEntity catalog = set(new CatalogEntity());
        catalog.setClassId(classId);
        catalog.setDeleteFlag(0);
        catalog.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return catalog;
    }

    public CatalogEntity update(CatalogEntity catalog){
        return set(catalog);
    }
}
