package com.njbandou.web.repository;

import com.njbandou.web.entity.CoursewareTypeEntity;
import java.util.Optional;

public interface CoursewareTypeRepository extends BaseRepository<CoursewareTypeEntity> {
    Optional<CoursewareTypeEntity> findByName(String name);
}
