package com.njbandou.web.mapper;

import com.njbandou.web.entity.QTitleEntity;
import com.njbandou.web.vo.item.TitleResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TitleMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QTitleEntity title = QTitleEntity.titleEntity;

    public QueryResults<TitleResult> selectAll(Integer[] exceptTitleIds) {
        QBean<TitleResult> columns = Projections.bean(TitleResult.class,
                title.pkId,
                title.name,
                title.sort);

        JPAQuery<TitleResult> query = jpaQueryFactory.select(columns).from(title);

        if (exceptTitleIds != null && exceptTitleIds.length != 0){
            query.where(title.pkId.notIn(exceptTitleIds));
        }

        return query.orderBy(title.sort.asc())
                .fetchResults();
    }

    public QueryResults<TitleResult> selectByIds(Integer[] titleIds) {
        QBean<TitleResult> columns = Projections.bean(TitleResult.class,
                title.pkId,
                title.name,
                title.sort);

        JPAQuery<TitleResult> query = jpaQueryFactory.select(columns).from(title)
                .where(title.pkId.in(titleIds));

        return query.orderBy(title.sort.asc())
                .fetchResults();
    }
}
