package com.njbandou.web.repository;

import com.njbandou.web.entity.QuestionBankTypeEntity;

import java.util.Optional;

public interface QuestionBankTypeRepository extends BaseRepository<QuestionBankTypeEntity> {
    Optional<QuestionBankTypeEntity> findByName(String name);
}
