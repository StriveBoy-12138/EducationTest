package com.njbandou.web.repository;

import com.njbandou.web.entity.OrganizationEntity;

import java.util.Optional;

public interface OrganizationRepository extends BaseRepository<OrganizationEntity> {
    Optional<OrganizationEntity> findByParentIdAndName(Integer parentId, String name);
}
