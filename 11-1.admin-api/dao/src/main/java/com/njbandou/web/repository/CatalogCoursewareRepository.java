package com.njbandou.web.repository;

import com.njbandou.web.entity.CatalogCoursewareEntity;

import java.util.Optional;

public interface CatalogCoursewareRepository extends BaseRepository<CatalogCoursewareEntity> {
    void deleteByCatalogId(Integer catalogId);

    Optional<CatalogCoursewareEntity> findByCatalogIdAndSort(Integer catalogId, Integer sort);

    Optional<CatalogCoursewareEntity> findFirstByCatalogIdAndSortAfterAndDeleteFlagOrderBySortAsc(Integer catalogId, Integer sort, Integer deleteFlag);

    Optional<CatalogCoursewareEntity> findFirstByCatalogIdAndSortBeforeAndDeleteFlagOrderBySortDesc(Integer catalogId, Integer sort, Integer deleteFlag);

    Optional<CatalogCoursewareEntity> findFirstByCatalogIdAndDeleteFlagOrderBySortDesc(Integer catalogId, Integer deleteFlag);
}
