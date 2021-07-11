package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.CatalogCoursewareEntity;
import io.swagger.annotations.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@ApiModel(value = "目录课件表单条数据dto",description = "从客户端，由用户传来的数据封装在此dto中")
public class CatalogCoursewareDTO {
    @ApiModelProperty(name = "pkId",value = "目录课件表主键pk_id")
    private Integer pkId;
    @NotNull(message = "目录id不能为空")
    @ApiModelProperty(name = "catalogId",value = "目录表主键pk_id")
    private Integer catalogId;
    @NotNull(message = "课件id不能为空")
    @ApiModelProperty(name = "coursewareId",value = "课件表主键pk_id")
    private Integer coursewareId;
    @ApiModelProperty(name = "sort",value = "int类型用于排序")
    private Integer sort;
    @ApiModelProperty(name = "move",value = "移动标识(up:上移 down:下移)")
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
