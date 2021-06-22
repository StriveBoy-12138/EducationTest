package com.njbandou.web.mapper;

import com.njbandou.web.dto.search.SearchCatalogCoursewareDTO;
import com.njbandou.web.entity.QCatalogCoursewareEntity;
import com.njbandou.web.entity.QCoursewareEntity;
import com.njbandou.web.vo.item.CatalogCoursewareResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CatalogCoursewareMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QCatalogCoursewareEntity catalogCourseware = QCatalogCoursewareEntity.catalogCoursewareEntity;
    private static QCoursewareEntity courseware = QCoursewareEntity.coursewareEntity;

    public QueryResults<CatalogCoursewareResult> selectAll(SearchCatalogCoursewareDTO searchCatalogCoursewareDTO) {
        QBean<CatalogCoursewareResult> columns = Projections.bean(CatalogCoursewareResult.class,
                catalogCourseware.pkId,
                catalogCourseware.catalogId,
                catalogCourseware.coursewareId,
                courseware.title.as("coursewareTitle"),
                courseware.studyTime,
                catalogCourseware.commentTime,
                catalogCourseware.sort);

        JPAQuery<CatalogCoursewareResult> query = jpaQueryFactory.select(columns).from(catalogCourseware)
                .where(catalogCourseware.catalogId.eq(searchCatalogCoursewareDTO.getCatalogId()))
                .leftJoin(courseware).on(catalogCourseware.coursewareId.eq(courseware.pkId));

        return query.orderBy(catalogCourseware.sort.asc())
                .fetchResults();
    }
}
