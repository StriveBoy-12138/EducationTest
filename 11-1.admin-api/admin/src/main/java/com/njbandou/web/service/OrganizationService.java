package com.njbandou.web.service;

import com.njbandou.web.dto.modify.OrganizationDTO;
import com.njbandou.web.vo.Result;

public interface OrganizationService {
    /**
     * 获取所有组织架构
     * @return
     */
    Result getAll(Integer[] exceptOrganizationIds);

    /**
     * 获取邀请组织架构
     * @return
     */
    Result getInviteOrganization(Integer[] organizationIds);

    /**
     * 添加组织架构
     * @param organizationDTO
     * @return
     */
    Result add(OrganizationDTO organizationDTO);

    /**
     * 修改组织架构
     * @param organizationDTO
     * @return
     */
    Result update(OrganizationDTO organizationDTO);

    /**
     * 删除组织架构
     * @param organizationDTO
     * @return
     */
    Result delete(OrganizationDTO organizationDTO);
}
