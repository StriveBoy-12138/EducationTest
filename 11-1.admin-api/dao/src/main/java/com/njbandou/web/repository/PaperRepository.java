package com.njbandou.web.repository;

import com.njbandou.web.entity.PaperEntity;
import java.util.Optional;

public interface PaperRepository extends BaseRepository<PaperEntity> {
    Optional<PaperEntity> findByTitle(String title);

    Integer countDistinctByDeleteFlag(Integer deleteFlag);
}
