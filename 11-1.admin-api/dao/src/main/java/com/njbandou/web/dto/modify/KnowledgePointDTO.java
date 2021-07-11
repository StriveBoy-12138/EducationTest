package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.KnowledgePointEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@ApiModel(value = "知识点对象DTO", description = "从客户端，由用户传入的数据封装在此DTO中")
@Data
public class KnowledgePointDTO {
    @ApiModelProperty(value = "题目主键", name = "pkId")
    private Integer pkId;
    @ApiModelProperty(value = "知识点分类", name = "typeId")
    private Integer typeId;
    @ApiModelProperty(value = "名称", name = "name")
    @NotBlank(message = "名称不能为空")
    private String name;
    @ApiModelProperty(value = "创建人", name = "authorId")
    private Integer authorId;

    private KnowledgePointEntity set(KnowledgePointEntity knowledgePoint){
        knowledgePoint.setTypeId(typeId);
        knowledgePoint.setName(name);
        knowledgePoint.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return knowledgePoint;
    }

    public KnowledgePointEntity insert(){
        KnowledgePointEntity knowledgePoint = set(new KnowledgePointEntity());
        knowledgePoint.setAuthorId(authorId);
        knowledgePoint.setDeleteFlag(0);
        knowledgePoint.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return knowledgePoint;
    }

    public KnowledgePointEntity update(KnowledgePointEntity knowledgePoint){
        return set(knowledgePoint);
    }
}
