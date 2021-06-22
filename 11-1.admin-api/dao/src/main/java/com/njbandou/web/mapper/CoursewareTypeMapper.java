package com.njbandou.web.mapper;

import com.njbandou.web.entity.QCoursewareTypeEntity;
import com.njbandou.web.vo.item.CoursewareTypeResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CoursewareTypeMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QCoursewareTypeEntity coursewareType = QCoursewareTypeEntity.coursewareTypeEntity;

    public QueryResults<CoursewareTypeResult> selectAll() {
        QBean<CoursewareTypeResult> columns = Projections.bean(CoursewareTypeResult.class,
                coursewareType.pkId,
                coursewareType.name);

        JPAQuery<CoursewareTypeResult> query = jpaQueryFactory.select(columns).from(coursewareType);

        return query.orderBy(coursewareType.pkId.asc())
                .fetchResults();
    }
}
