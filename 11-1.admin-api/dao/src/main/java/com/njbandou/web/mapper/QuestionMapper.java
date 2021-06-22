package com.njbandou.web.mapper;

import com.njbandou.web.dto.search.SearchQuestionDTO;
import com.njbandou.web.dto.search.SearchRuleDTO;
import com.njbandou.web.entity.QQuestionEntity;
import com.njbandou.web.vo.item.QuestionResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QQuestionEntity question = QQuestionEntity.questionEntity;

    public QueryResults<QuestionResult> selectByPage(SearchQuestionDTO searchQuestionDTO) {
        QBean<QuestionResult> columns = Projections.bean(QuestionResult.class,
                question.pkId,
                question.bankId,
                question.questionId,
                question.type,
                question.title,
                question.knowledgePoint,
                question.audioName,
                question.audio,
                question.videoName,
                question.video,
                question.fileName,
                question.file,
                question.analysis,
                question.analysisAudioName,
                question.analysisAudio,
                question.analysisVideoName,
                question.analysisVideo,
                question.answer,
                question.difficulty,
                question.createTime,
                question.updateTime);

        JPAQuery<QuestionResult> query = jpaQueryFactory.select(columns).from(question)
                .where(question.questionId.eq(searchQuestionDTO.getQuestionId()));

        if (!searchQuestionDTO.getBankId().equals(0)){
            query.where(question.bankId.eq(searchQuestionDTO.getBankId()));
        }
        if (!searchQuestionDTO.getType().equals(-1)){
            query.where(question.type.eq(searchQuestionDTO.getType()));
        }
        if (!searchQuestionDTO.getDifficulty().equals(-1)){
            query.where(question.difficulty.eq(searchQuestionDTO.getDifficulty()));
        }
        if (StringUtils.isNotBlank(searchQuestionDTO.getTitle())){
            query.where(question.title.contains(searchQuestionDTO.getTitle()));
        }

        return query.orderBy(question.pkId.desc())
                .offset(searchQuestionDTO.getPage())
                .limit(searchQuestionDTO.getPageSize())
                .fetchResults();
    }

    public QueryResults<QuestionResult> selectByRules(Integer ruleId, SearchRuleDTO searchRuleDTO) {
        QBean<QuestionResult> columns = Projections.bean(QuestionResult.class,
                question.pkId,
                question.bankId,
                question.questionId,
                question.type,
                question.title,
                question.knowledgePoint,
                question.audioName,
                question.audio,
                question.videoName,
                question.video,
                question.fileName,
                question.file,
                question.analysis,
                question.analysisAudioName,
                question.analysisAudio,
                question.analysisVideoName,
                question.analysisVideo,
                question.answer,
                question.difficulty,
                question.createTime,
                question.updateTime);

        JPAQuery<QuestionResult> query = jpaQueryFactory.select(columns).from(question)
                .where(question.questionId.eq(0));

        if (ruleId.equals(0)){
            query.where(question.type.in(searchRuleDTO.getType()));
        }else if (ruleId.equals(1)) {
            query.where(question.difficulty.in(searchRuleDTO.getType()));
        }else if (ruleId.equals(2)) {
            query.where(question.bankId.in(searchRuleDTO.getType()));
        }

        //随机排序
        NumberExpression<Double> doubleNumberExpression = Expressions.numberTemplate(Double.class, "rand()");
        OrderSpecifier<Double> doubleOrderSpecifier = new OrderSpecifier<>(Order.ASC, doubleNumberExpression);

        return query.orderBy(doubleOrderSpecifier)
                .limit(searchRuleDTO.getNum())
                .fetchResults();
    }
}
