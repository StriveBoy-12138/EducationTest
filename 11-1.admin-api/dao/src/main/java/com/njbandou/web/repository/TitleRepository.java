package com.njbandou.web.repository;

import com.njbandou.web.entity.TitleEntity;

import java.util.Optional;

public interface TitleRepository extends BaseRepository<TitleEntity> {
    Optional<TitleEntity> findByName(String name);
    
    Optional<TitleEntity> findBySort(Integer sort);

    Optional<TitleEntity> findFirstBySortAfterAndDeleteFlagOrderBySortAsc(Integer sort, Integer deleteFlag);

    Optional<TitleEntity> findFirstBySortBeforeAndDeleteFlagOrderBySortDesc(Integer sort, Integer deleteFlag);

    Optional<TitleEntity> findFirstByDeleteFlagOrderBySortDesc(Integer deleteFlag);
}
