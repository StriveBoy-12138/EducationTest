package com.njbandou.web.repository;

import com.njbandou.web.entity.QuestionEntity;

import java.util.Optional;

public interface QuestionRepository extends BaseRepository<QuestionEntity> {
    Optional<QuestionEntity> findByTitleAndBankId(String title, Integer bankId);

    Integer countDistinctByBankId(Integer bankId);

    Integer countDistinctByType(Integer type);

    Integer countDistinctByDifficulty(Integer difficulty);

    Integer countDistinctByDeleteFlag(Integer deleteFlag);
}
