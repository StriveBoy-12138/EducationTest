package com.njbandou.web.mapper;

import com.njbandou.web.dto.search.SearchKnowledgePointDTO;
import com.njbandou.web.entity.QKnowledgePointEntity;
import com.njbandou.web.entity.QKnowledgePointTypeEntity;
import com.njbandou.web.entity.QUserEntity;
import com.njbandou.web.vo.item.KnowledgePointResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class KnowledgePointMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QKnowledgePointEntity knowledgePoint = QKnowledgePointEntity.knowledgePointEntity;
    private static QKnowledgePointTypeEntity knowledgePointType = QKnowledgePointTypeEntity.knowledgePointTypeEntity;
    private static QUserEntity user = QUserEntity.userEntity;

    public QueryResults<KnowledgePointResult> selectByPage(SearchKnowledgePointDTO searchKnowledgePointDTO) {
        QBean<KnowledgePointResult> columns = Projections.bean(KnowledgePointResult.class,
                knowledgePoint.pkId,
                knowledgePoint.name,
                knowledgePoint.typeId,
                knowledgePointType.name.as("typeName"),
                knowledgePoint.authorId,
                user.nickname.as("authorName"),
                knowledgePoint.createTime);

        JPAQuery<KnowledgePointResult> query = jpaQueryFactory.select(columns).from(knowledgePoint)
                .leftJoin(knowledgePointType).on(knowledgePoint.typeId.eq(knowledgePointType.pkId))
                .leftJoin(user).on(knowledgePoint.authorId.eq(user.pkId));

        if (!searchKnowledgePointDTO.getTypeId().equals(0)){
            query.where(knowledgePoint.typeId.eq(searchKnowledgePointDTO.getTypeId()));
        }
        if (StringUtils.isNotBlank(searchKnowledgePointDTO.getName())){
            query.where(knowledgePoint.name.contains(searchKnowledgePointDTO.getName()));
        }
        if (searchKnowledgePointDTO.getExceptKnowledgePointIds() != null && searchKnowledgePointDTO.getExceptKnowledgePointIds().length != 0){
            query.where(knowledgePoint.pkId.notIn(searchKnowledgePointDTO.getExceptKnowledgePointIds()));
        }

        return query.orderBy(knowledgePoint.pkId.desc())
                .offset(searchKnowledgePointDTO.getPage())
                .limit(searchKnowledgePointDTO.getPageSize())
                .fetchResults();
    }
}
