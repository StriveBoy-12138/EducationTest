package com.njbandou.web.repository;

import com.njbandou.web.entity.KnowledgePointTypeEntity;

import java.util.Optional;

public interface KnowledgePointTypeRepository extends BaseRepository<KnowledgePointTypeEntity> {
    Optional<KnowledgePointTypeEntity> findByName(String name);
}
