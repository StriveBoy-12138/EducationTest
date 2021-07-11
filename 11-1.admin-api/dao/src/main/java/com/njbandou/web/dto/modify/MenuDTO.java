package com.njbandou.web.dto.modify;

import com.njbandou.web.dto.search.BaseSearchDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel(value = "权限对象DTO", description = "从客户端，由用户传入的数据封装在此DTO中")
@Data
public class MenuDTO{
    @ApiModelProperty(value = "权限主键", name = "pkId")
    private Integer pkId;
    @ApiModelProperty(value = "父级id", name = "parentId")
    private Integer parentId;
    @ApiModelProperty(value = "名称", name = "name")
    private String name;
    @ApiModelProperty(value = "名称", name = "title")
    private String title;
    @ApiModelProperty(value = "序号", name = "orderNum")
    private Integer orderNum;

    @Override
    public String toString() {
        return "MenuDTO{" +
                "pkId=" + pkId +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", orderNum=" + orderNum +
                '}';
    }
}

