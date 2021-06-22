package com.njbandou.web.repository;

import com.njbandou.web.entity.CatalogEntity;

import java.util.Optional;

public interface CatalogRepository extends BaseRepository<CatalogEntity> {
    Optional<CatalogEntity> findByClassIdAndTitle(Integer classId, String title);

    Optional<CatalogEntity> findByClassIdAndSort(Integer classId, Integer sort);

    Optional<CatalogEntity> findFirstByClassIdAndSortAfterAndDeleteFlagOrderBySortAsc(Integer classId, Integer sort, Integer deleteFlag);

    Optional<CatalogEntity> findFirstByClassIdAndSortBeforeAndDeleteFlagOrderBySortDesc(Integer classId, Integer sort, Integer deleteFlag);

    Optional<CatalogEntity> findFirstByClassIdAndDeleteFlagOrderBySortDesc(Integer classId, Integer deleteFlag);
}
