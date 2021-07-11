package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.PaperTypeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
@ApiModel(value = "试卷分类对象DTO", description = "从客户端，由用户传入的数据封装在此DTO中")
@Data
public class PaperTypeDTO {
    @ApiModelProperty(value = "试卷分类主键", name = "pkId")
    private Integer pkId;
    @ApiModelProperty(value = "试卷分类名称", name = "name")
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
