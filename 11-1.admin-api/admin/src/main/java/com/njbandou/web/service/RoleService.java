package com.njbandou.web.service;


import com.njbandou.web.dto.modify.RoleDTO;
import com.njbandou.web.dto.search.SearchRoleDTO;
import com.njbandou.web.vo.Result;

import java.util.Map;

public interface RoleService {
    /**
     * 获取角色列表
     * @param searchRoleDTO 搜索条件
     * @return Result
     */
    Result getPage(SearchRoleDTO searchRoleDTO);

    /**
     * 获取所有角色
     * @return Result
     */
    Result getAll();

    /**
     * 添加角色和权限
     * @param roleDTO 角色信息
     * @return Result
     */
    Result add(RoleDTO roleDTO);

    /**
     * 更新角色和权限
     * @param roleDTO 角色信息
     * @return Result
     */
    Result update(RoleDTO roleDTO);

    /**
     * 删除角色（批量）
     * @param ids 角色id集合
     * @return Result
     */
    Result delete(Integer[] ids);

    /**
     * 获取角色详情
     * @param roleId 角色id
     * @return Map
     */
    Map info(Integer roleId);
}
