package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.InformationCollectionEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class InformationCollectionDTO {
    private Integer pkId;

    private Integer relationId;

    @NotNull(message = "信息类型不能为空")
    private Integer type;

    @NotBlank(message = "信息名称不能为空")
    private String title;

    @NotNull(message = "是否必填不能为空")
    private Integer isRequired;

    private Integer isAble;

    private String option = "[]";

    private Integer sort;

    private String move;

    private InformationCollectionEntity set(InformationCollectionEntity informationCollection){
        informationCollection.setType(type);
        informationCollection.setTitle(title);
        informationCollection.setIsRequired(isRequired);
        informationCollection.setIsAble(isAble);
        informationCollection.setOption(option);
        informationCollection.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return informationCollection;
    }

    public InformationCollectionEntity insert(){
        InformationCollectionEntity informationCollection = set(new InformationCollectionEntity());
        informationCollection.setRelationId(relationId);
        informationCollection.setDeleteFlag(0);
        informationCollection.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return informationCollection;
    }

    public InformationCollectionEntity update(InformationCollectionEntity informationCollection){
        return set(informationCollection);
    }
}
