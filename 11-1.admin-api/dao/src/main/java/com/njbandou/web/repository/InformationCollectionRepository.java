package com.njbandou.web.repository;

import com.njbandou.web.entity.InformationCollectionEntity;

import java.util.Optional;

public interface InformationCollectionRepository extends BaseRepository<InformationCollectionEntity> {
    Optional<InformationCollectionEntity> findByRelationIdAndTitle(Integer relationId, String title);

    Optional<InformationCollectionEntity> findByRelationIdAndSort(Integer relationId, Integer sort);

    Optional<InformationCollectionEntity> findFirstByRelationIdAndSortAfterAndDeleteFlagOrderBySortAsc(Integer relationId,Integer sort, Integer deleteFlag);

    Optional<InformationCollectionEntity> findFirstByRelationIdAndSortBeforeAndDeleteFlagOrderBySortDesc(Integer relationIdm, Integer sort, Integer deleteFlag);

    Optional<InformationCollectionEntity> findFirstByRelationIdAndDeleteFlagOrderBySortDesc(Integer relationId, Integer deleteFlag);
}
