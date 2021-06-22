package com.njbandou.web.repository;

import com.njbandou.web.entity.UserRoleEntity;

public interface UserRoleRepository extends BaseRepository<UserRoleEntity> {
    UserRoleEntity findByUserId(Integer userId);
}
