package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.PaperTypeEntity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class PaperTypeDTO {
    private Integer pkId;
    private String name;

    private PaperTypeEntity set(PaperTypeEntity paperType){
        paperType.setName(name);
        paperType.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return paperType;
    }

    public PaperTypeEntity insert(){
        PaperTypeEntity paperType = set(new PaperTypeEntity());
        paperType.setDeleteFlag(0);
        paperType.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return paperType;
    }

    public PaperTypeEntity update(PaperTypeEntity paperType){
        return set(paperType);
    }
}
