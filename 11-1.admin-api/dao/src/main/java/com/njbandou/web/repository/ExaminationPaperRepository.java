package com.njbandou.web.repository;

import com.njbandou.web.entity.ExaminationPaperEntity;

public interface ExaminationPaperRepository extends BaseRepository<ExaminationPaperEntity> {
    Integer countDistinctByPaperIdAndStatus(Integer paperId, Integer status);

    void deleteByPaperId(Integer paperId);
}
