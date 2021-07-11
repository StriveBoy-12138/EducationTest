package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.ClassTypeEntity;
import io.swagger.annotations.*;
import lombok.Data;

import java.sql.Timestamp;
@Data
@ApiModel(value = "课程表分类单条数据dto",description = "从客户端，由用户传来的数据封装在此dto中")
public class ClassTypeDTO {
    @ApiModelProperty(name ="pk_id",value = "课程分类表主键id")
    private Integer pkId;
    @ApiModelProperty(name ="name",value = "课程分类名称")
    private String name;

    private ClassTypeEntity set(ClassTypeEntity classType){
        classType.setName(name);
        classType.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return classType;
    }

    public ClassTypeEntity insert(){
        ClassTypeEntity classType = set(new ClassTypeEntity());
        classType.setDeleteFlag(0);
        classType.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return classType;
    }

    public ClassTypeEntity update(ClassTypeEntity classType){
        return set(classType);
    }
}
