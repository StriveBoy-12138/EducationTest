package com.njbandou.web.repository;

import com.njbandou.web.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<UserEntity> {
    Optional<UserEntity> findByAccount(String account);

    List<UserEntity> findByPkIdIn(Integer[] userIds);

    List<UserEntity> findByTitleIn(Integer[] titleIds);

    List<UserEntity> findByOrganizationIdIn(Integer[] organizationIds);

    Integer countDistinctByDeleteFlag(Integer deleteFlag);
}
