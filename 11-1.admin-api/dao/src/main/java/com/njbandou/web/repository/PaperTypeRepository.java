package com.njbandou.web.repository;

import com.njbandou.web.entity.PaperTypeEntity;

import java.util.Optional;

public interface PaperTypeRepository extends BaseRepository<PaperTypeEntity> {
    Optional<PaperTypeEntity> findByName(String name);
}
