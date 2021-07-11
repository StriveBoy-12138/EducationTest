package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.TitleEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@ApiModel(value = "标签对象DTO", description = "从客户端，由用户传入的数据封装在此DTO中")
@Data
public class TitleDTO {

    @ApiModelProperty(value = "标签主键", name = "pkId")
    private Integer pkId;

    @ApiModelProperty(value = "标签名称", name = "name")
    @NotBlank(message = "标签名称不能为空")
    private String name;

    @ApiModelProperty(value = "排序序号", name = "sort")
    private Integer sort;

    @ApiModelProperty(value = "上移或下移标志", name = "move")
    private String move;

    private TitleEntity set(TitleEntity title){
        title.setName(name);
        title.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return title;
    }

    public TitleEntity insert(){
        TitleEntity title = set(new TitleEntity());
        title.setDeleteFlag(0);
        title.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return title;
    }

    public TitleEntity update(TitleEntity title){
        return set(title);
    }
}
