package com.njbandou.web.repository;

import com.njbandou.web.entity.RoleMenuEntity;

import java.util.List;

public interface RoleMenuRepository extends BaseRepository<RoleMenuEntity> {
    List<RoleMenuEntity> findByRoleId(Integer roleId);

    void deleteByRoleId(Integer roleId);
}
