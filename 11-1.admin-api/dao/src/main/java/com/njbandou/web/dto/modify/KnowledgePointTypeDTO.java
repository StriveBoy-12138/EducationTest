package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.KnowledgePointTypeEntity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class KnowledgePointTypeDTO {
    private Integer pkId;
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
