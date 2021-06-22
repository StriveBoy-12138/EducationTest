package com.njbandou.web.mapper;

import com.njbandou.web.entity.QPaperTypeEntity;
import com.njbandou.web.vo.item.PaperTypeResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PaperTypeMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QPaperTypeEntity paperType = QPaperTypeEntity.paperTypeEntity;

    public QueryResults<PaperTypeResult> selectAll() {
        QBean<PaperTypeResult> columns = Projections.bean(PaperTypeResult.class,
                paperType.pkId,
                paperType.name);

        JPAQuery<PaperTypeResult> query = jpaQueryFactory.select(columns).from(paperType);

        return query.orderBy(paperType.pkId.asc())
                .fetchResults();
    }
}
