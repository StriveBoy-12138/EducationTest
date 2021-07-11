package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.QuestionBankEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

@ApiModel(value = "题库信息对象DTO", description = "从客户端，由用户传入的数据封装在此DTO中")
@Data
public class QuestionBankDTO {
    @ApiModelProperty(value = "题库信息主键", name = "pkId")
    private Integer pkId;
    @ApiModelProperty(value = "题库分类id", name = "typeId")
    private Integer typeId;
    @ApiModelProperty(value = "题库名称", name = "name")
    private String name;
    @ApiModelProperty(value = "题库描述", name = "des")
    private String des;
    @ApiModelProperty(value = "图片", name = "picture")
    private String picture;
    @ApiModelProperty(value = "创建人id", name = "authorId")
    private Integer authorId;

    private QuestionBankEntity set(QuestionBankEntity questionBank){
        questionBank.setTypeId(typeId);
        questionBank.setName(name);
        questionBank.setDes(des);
        questionBank.setPicture(picture);
        questionBank.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return questionBank;
    }

    public QuestionBankEntity insert(){
        QuestionBankEntity questionBank = set(new QuestionBankEntity());
        questionBank.setAuthorId(authorId);
        questionBank.setDeleteFlag(0);
        questionBank.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return questionBank;
    }

    public QuestionBankEntity update(QuestionBankEntity questionBank){
        return set(questionBank);
    }
}
