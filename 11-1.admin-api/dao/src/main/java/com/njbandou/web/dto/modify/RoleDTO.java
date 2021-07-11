package com.njbandou.web.dto.modify;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@ApiModel(value = "角色信息对象DTO", description = "从客户端，由用户传入的数据封装在此DTO中")
@Data
public class RoleDTO {
    @ApiModelProperty(value = "角色主键", name = "pkId")
    private Integer pkId;
    @ApiModelProperty(value = "角色名称", name = "name")
    private String name;
    @ApiModelProperty(value = "角色信息描述", name = "remark")
    private String remark;
    @ApiModelProperty(value = "权限列表信息", name = "menuIds")
    private List<Integer> menuIds;
}
