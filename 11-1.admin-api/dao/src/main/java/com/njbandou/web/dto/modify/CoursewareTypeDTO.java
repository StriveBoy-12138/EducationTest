package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.CoursewareTypeEntity;
import io.swagger.annotations.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@ApiModel(value = "课件分类表单条数据dto",description = "从客户端，由用户传来的数据封装在此dto中")
public class CoursewareTypeDTO {

    @ApiModelProperty(name ="pk_id",value = "课件分类表主键id")
    private Integer pkId;
    @ApiModelProperty(name ="name",value = "课件分类名称")
    private String name;


    private CoursewareTypeEntity set(CoursewareTypeEntity coursewareType){
        coursewareType.setName(name);
        coursewareType.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return coursewareType;
    }

    public CoursewareTypeEntity insert(){
        CoursewareTypeEntity coursewareType = set(new CoursewareTypeEntity());
        coursewareType.setDeleteFlag(0);
        coursewareType.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return coursewareType;
    }

    public CoursewareTypeEntity update(CoursewareTypeEntity coursewareType){
        return set(coursewareType);
    }
}
