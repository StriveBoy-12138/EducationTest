package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.KnowledgePointEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class KnowledgePointDTO {
    private Integer pkId;
    private Integer typeId;
    @NotBlank(message = "名称不能为空")
    private String name;
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
