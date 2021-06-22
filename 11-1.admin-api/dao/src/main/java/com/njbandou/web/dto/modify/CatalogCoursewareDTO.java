package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.CatalogCoursewareEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class CatalogCoursewareDTO {
    private Integer pkId;
    @NotNull(message = "目录id不能为空")
    private Integer catalogId;
    @NotNull(message = "课件id不能为空")
    private Integer coursewareId;
    private Integer sort;
    private String move;

    private CatalogCoursewareEntity set(CatalogCoursewareEntity catalogCourseware){
        catalogCourseware.setCatalogId(catalogId);
        catalogCourseware.setCoursewareId(coursewareId);
        catalogCourseware.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return catalogCourseware;
    }

    public CatalogCoursewareEntity insert(){
        CatalogCoursewareEntity catalogCourseware = set(new CatalogCoursewareEntity());
        catalogCourseware.setCommentTime(0);
        catalogCourseware.setDeleteFlag(0);
        catalogCourseware.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return catalogCourseware;
    }

    public CatalogCoursewareEntity update(CatalogCoursewareEntity catalogCourseware){
        return set(catalogCourseware);
    }
}
