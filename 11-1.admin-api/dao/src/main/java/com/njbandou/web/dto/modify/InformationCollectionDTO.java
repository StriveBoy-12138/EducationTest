package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.InformationCollectionEntity;
import io.swagger.annotations.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
@ApiModel(value = "信息收集表单条数据dto", description = "从客户端，由用户传来的数据封装在此dto中")
@Data
public class InformationCollectionDTO {
    @ApiModelProperty(name ="pkId",value = "信息收集表主键")
    private Integer pkId;
    @ApiModelProperty(name ="relationId",value = "关联id")
    private Integer relationId;
    @ApiModelProperty(name ="type",value = "信息类型")
    @NotNull(message = "信息类型不能为空")
    private Integer type;
    @ApiModelProperty(name ="title",value = "信息名称")
    @NotBlank(message = "信息名称不能为空")
    private String title;
    @NotNull(message = "是否必填不能为空")
    @ApiModelProperty(name ="isAble",value = "是否必填(0:是 1:否)")
    private Integer isRequired;
    @ApiModelProperty(name ="isAble",value = "是否可填/唯一(0:是 1:否)")
    private Integer isAble;
    @ApiModelProperty(name ="option",value = "选项")
    private String option = "[]";
    @ApiModelProperty(name ="sort",value = "int用于排序")
    private Integer sort;
    @ApiModelProperty(name ="move",value = "标记上移下移{up:上移,down:下移")
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
