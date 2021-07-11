package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.QuestionBankTypeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

@ApiModel(value = "题库分类对象DTO", description = "从客户端，由用户传入的数据封装在此DTO中")
@Data
public class QuestionBankTypeDTO {
    @ApiModelProperty(value = "题库分类主键", name = "pkId")
    private Integer pkId;
    @ApiModelProperty(value = "题库分类名称", name = "name")
    private String name;

    private QuestionBankTypeEntity set(QuestionBankTypeEntity questionBankType){
        questionBankType.setName(name);
        questionBankType.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return questionBankType;
    }

    public QuestionBankTypeEntity insert(){
        QuestionBankTypeEntity questionBankType = set(new QuestionBankTypeEntity());
        questionBankType.setParentId(0);
        questionBankType.setDeleteFlag(0);
        questionBankType.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return questionBankType;
    }

    public QuestionBankTypeEntity update(QuestionBankTypeEntity questionBankType){
        return set(questionBankType);
    }
}
