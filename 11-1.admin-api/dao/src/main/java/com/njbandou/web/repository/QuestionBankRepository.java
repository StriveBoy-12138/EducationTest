package com.njbandou.web.repository;

import com.njbandou.web.entity.QuestionBankEntity;

import java.util.Optional;

public interface QuestionBankRepository extends BaseRepository<QuestionBankEntity> {
    Optional<QuestionBankEntity> findByName(String name);
}
