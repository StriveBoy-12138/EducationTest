package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.KnowledgePointTypeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
@ApiModel(value = "知识点分类对象DTO", description = "从客户端，由用户传入的数据封装在此DTO中")
@Data
public class KnowledgePointTypeDTO {
    @ApiModelProperty(value = "知识点分类主键", name = "pkId")
    private Integer pkId;
    @ApiModelProperty(value = "知识点分类名称", name = "name")
    private String name;

    private KnowledgePointTypeEntity set(KnowledgePointTypeEntity knowledgePointType){
        knowledgePointType.setName(name);
        knowledgePointType.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return knowledgePointType;
    }

    public KnowledgePointTypeEntity insert(){
        KnowledgePointTypeEntity knowledgePointType = set(new KnowledgePointTypeEntity());
        knowledgePointType.setDeleteFlag(0);
        knowledgePointType.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return knowledgePointType;
    }

    public KnowledgePointTypeEntity update(KnowledgePointTypeEntity knowledgePointType){
        return set(knowledgePointType);
    }
}
