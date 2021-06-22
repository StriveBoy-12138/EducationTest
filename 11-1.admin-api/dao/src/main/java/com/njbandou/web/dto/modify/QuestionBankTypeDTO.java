package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.QuestionBankTypeEntity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class QuestionBankTypeDTO {
    private Integer pkId;
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
