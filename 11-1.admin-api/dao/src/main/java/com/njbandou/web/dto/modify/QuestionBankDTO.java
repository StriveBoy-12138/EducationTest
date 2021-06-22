package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.QuestionBankEntity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class QuestionBankDTO {
    private Integer pkId;
    private Integer typeId;
    private String name;
    private String des;
    private String picture;
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
