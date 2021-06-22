package com.njbandou.web.mapper;

import com.njbandou.web.entity.QQuestionBankTypeEntity;
import com.njbandou.web.vo.item.QuestionBankTypeResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionBankTypeMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QQuestionBankTypeEntity questionBankType = QQuestionBankTypeEntity.questionBankTypeEntity;

    public QueryResults<QuestionBankTypeResult> selectAll() {
        QBean<QuestionBankTypeResult> columns = Projections.bean(QuestionBankTypeResult.class,
                questionBankType.pkId,
                questionBankType.name);

        JPAQuery<QuestionBankTypeResult> query = jpaQueryFactory.select(columns).from(questionBankType);

        return query.orderBy(questionBankType.pkId.asc())
                .fetchResults();
    }
}
