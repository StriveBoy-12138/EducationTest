package com.njbandou.web.repository;

import com.njbandou.web.entity.CoursewareEntity;

import java.util.Optional;

public interface CoursewareRepository extends BaseRepository<CoursewareEntity> {
    Optional<CoursewareEntity> findByTitle(String title);

    Integer countDistinctByDeleteFlag(Integer deleteFlag);
}
