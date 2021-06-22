package com.njbandou.web.mapper;

import com.njbandou.web.entity.QKnowledgePointTypeEntity;
import com.njbandou.web.vo.item.KnowledgePointTypeResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class KnowledgePointTypeMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QKnowledgePointTypeEntity knowledgePointType = QKnowledgePointTypeEntity.knowledgePointTypeEntity;

    public QueryResults<KnowledgePointTypeResult> selectAll() {
        QBean<KnowledgePointTypeResult> columns = Projections.bean(KnowledgePointTypeResult.class,
                knowledgePointType.pkId,
                knowledgePointType.name);

        JPAQuery<KnowledgePointTypeResult> query = jpaQueryFactory.select(columns).from(knowledgePointType);

        return query.orderBy(knowledgePointType.pkId.asc())
                .fetchResults();
    }
}
