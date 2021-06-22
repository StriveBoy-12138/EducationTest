package com.njbandou.web.repository;

import com.njbandou.web.entity.KnowledgePointEntity;
import com.njbandou.web.vo.item.QuestionKnowledgePointResult;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KnowledgePointRepository extends BaseRepository<KnowledgePointEntity> {
    Optional<KnowledgePointEntity> findByName(String name);

    @Query(value = "select new com.njbandou.web.vo.item.QuestionKnowledgePointResult(pkId,name) from KnowledgePointEntity where pkId in ?1")
    List<QuestionKnowledgePointResult> findByIds(Integer[] ids);
}
