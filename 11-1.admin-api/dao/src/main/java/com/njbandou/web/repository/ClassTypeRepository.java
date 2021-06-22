package com.njbandou.web.repository;

import com.njbandou.web.entity.ClassTypeEntity;

import java.util.Optional;

public interface ClassTypeRepository extends BaseRepository<ClassTypeEntity> {
    Optional<ClassTypeEntity> findByName(String name);
}
