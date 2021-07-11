package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.CatalogEntity;
import io.swagger.annotations.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@ApiModel(value = "目录表单条数据dto",description = "从客户端，由用户传来的数据封装在此dto中")
@Data
public class CatalogDTO {
    @ApiModelProperty(name ="pk_id",value = "目录表主键")
    private Integer pkId;
    @NotNull(message = "课程id不能为空")
    @ApiModelProperty(name = "课程id",value="课程表主键pk_id")
    private Integer classId;
    @ApiModelProperty(name="title" ,value ="目录名称")
    private String title;
    @ApiModelProperty(name="sort" ,value ="排序")
    private Integer sort;
    @ApiModelProperty(name="move" ,value ="移动标识(up:上移 down:下移)")
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
