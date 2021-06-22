package com.njbandou.web.mapper;

import com.njbandou.web.dto.search.SearchInformationCollectionDTO;
import com.njbandou.web.entity.QInformationCollectionEntity;
import com.njbandou.web.vo.item.InformationCollectionResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InformationCollectionMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QInformationCollectionEntity informationCollection = QInformationCollectionEntity.informationCollectionEntity;

    public QueryResults<InformationCollectionResult> selectAll(SearchInformationCollectionDTO searchInformationCollectionDTO){
        QBean<InformationCollectionResult> columns = Projections.bean(InformationCollectionResult.class,
                informationCollection.pkId,
                informationCollection.relationId,
                informationCollection.type,
                informationCollection.title,
                informationCollection.isRequired,
                informationCollection.isAble,
                informationCollection.option,
                informationCollection.sort);

        JPAQuery<InformationCollectionResult> query = jpaQueryFactory.select(columns).from(informationCollection)
                .where(informationCollection.relationId.eq(searchInformationCollectionDTO.getRelationId()));

        return query.orderBy(informationCollection.sort.asc())
                .fetchResults();
    }
}
