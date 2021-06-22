package com.njbandou.web.mapper;

import com.njbandou.web.dto.search.SearchQuestionBankDTO;
import com.njbandou.web.entity.*;
import com.njbandou.web.vo.item.QuestionBankResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionBankMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QQuestionBankEntity questionBank = QQuestionBankEntity.questionBankEntity;
    private static QQuestionBankTypeEntity questionBankType = QQuestionBankTypeEntity.questionBankTypeEntity;
    private static QUserEntity user = QUserEntity.userEntity;

    public QueryResults<QuestionBankResult> selectByPage(SearchQuestionBankDTO searchQuestionBankDTO) {
        QBean<QuestionBankResult> columns = Projections.bean(QuestionBankResult.class,
                questionBank.pkId,
                questionBank.typeId,
                questionBankType.name.as("typeName"),
                questionBank.name,
                questionBank.des,
                questionBank.picture,
                questionBank.authorId,
                user.nickname.as("authorName"),
                questionBank.createTime);

        JPAQuery<QuestionBankResult> query = jpaQueryFactory.select(columns).from(questionBank)
                .leftJoin(questionBankType).on(questionBank.typeId.eq(questionBankType.pkId))
                .leftJoin(user).on(questionBank.authorId.eq(user.pkId));

        if (!searchQuestionBankDTO.getTypeId().equals(0)){
            query.where(questionBank.typeId.eq(searchQuestionBankDTO.getTypeId()));
        }
        if (StringUtils.isNotBlank(searchQuestionBankDTO.getName())){
            query.where(questionBank.name.contains(searchQuestionBankDTO.getName()));
        }

        return query.orderBy(questionBank.pkId.desc())
                .offset(searchQuestionBankDTO.getPage())
                .limit(searchQuestionBankDTO.getPageSize())
                .fetchResults();
    }
}
