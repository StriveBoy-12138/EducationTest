package com.njbandou.web.repository;

import com.njbandou.web.entity.ExaminationEntity;

import java.util.Optional;

public interface ExaminationRepository extends BaseRepository<ExaminationEntity> {
    Optional<ExaminationEntity> findByPaperId(Integer paperId);
}
