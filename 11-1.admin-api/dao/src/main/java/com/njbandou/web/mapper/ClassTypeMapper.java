package com.njbandou.web.mapper;

import com.njbandou.web.entity.QClassTypeEntity;
import com.njbandou.web.vo.item.ClassTypeResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ClassTypeMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QClassTypeEntity classType = QClassTypeEntity.classTypeEntity;

    public QueryResults<ClassTypeResult> selectAll() {
        QBean<ClassTypeResult> columns = Projections.bean(ClassTypeResult.class,
                classType.pkId,
                classType.name);

        JPAQuery<ClassTypeResult> query = jpaQueryFactory.select(columns).from(classType);

        return query.orderBy(classType.pkId.asc())
                .fetchResults();
    }
}
