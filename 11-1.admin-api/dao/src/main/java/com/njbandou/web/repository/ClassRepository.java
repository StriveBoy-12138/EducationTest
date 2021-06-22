package com.njbandou.web.repository;

import com.njbandou.web.entity.ClassEntity;

import java.util.Optional;

public interface ClassRepository extends BaseRepository<ClassEntity> {
    Optional<ClassEntity> findByTitle(String title);

    Integer countDistinctByDeleteFlag(Integer deleteFlag);
}
