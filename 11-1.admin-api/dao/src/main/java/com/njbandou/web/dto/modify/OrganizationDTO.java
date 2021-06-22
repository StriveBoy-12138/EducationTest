package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.OrganizationEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class OrganizationDTO {
    private Integer pkId;

    @NotNull(message = "父级id不能为空")
    private Integer parentId;

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
