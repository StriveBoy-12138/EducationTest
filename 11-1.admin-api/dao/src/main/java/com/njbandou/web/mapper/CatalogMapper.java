package com.njbandou.web.mapper;

import com.njbandou.web.dto.search.SearchCatalogDTO;
import com.njbandou.web.entity.QCatalogEntity;
import com.njbandou.web.vo.item.CatalogResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CatalogMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QCatalogEntity catalog = QCatalogEntity.catalogEntity;

    public QueryResults<CatalogResult> selectAll(SearchCatalogDTO searchCatalogDTO) {
        QBean<CatalogResult> columns = Projections.bean(CatalogResult.class,
                catalog.pkId,
                catalog.classId,
                catalog.title,
                catalog.sort);

        JPAQuery<CatalogResult> query = jpaQueryFactory.select(columns).from(catalog)
                .where(catalog.classId.eq(searchCatalogDTO.getClassId()));

        return query.orderBy(catalog.sort.asc())
                .fetchResults();
    }
}
