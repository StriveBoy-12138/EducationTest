package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.OrganizationEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
@ApiModel(value = "组织架构对象DTO", description = "从客户端，由用户传入的数据封装在此DTO中")
@Data
public class OrganizationDTO {
    @ApiModelProperty(value = "组织主键", name = "pkId")
    private Integer pkId;

    @ApiModelProperty(value = "父级id", name = "parentId")
    @NotNull(message = "父级id不能为空")
    private Integer parentId;
    @ApiModelProperty(value = "名称", name = "name")
    @NotBlank(message = "名称不能为空")
    private String name;

    private OrganizationEntity set(OrganizationEntity organization){
        organization.setName(name);
        organization.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return organization;
    }

    public OrganizationEntity insert(){
        OrganizationEntity organization = set(new OrganizationEntity());
        organization.setParentId(parentId);
        organization.setDeleteFlag(0);
        organization.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return organization;
    }

    public OrganizationEntity update(OrganizationEntity organization){
        return set(organization);
    }

}
