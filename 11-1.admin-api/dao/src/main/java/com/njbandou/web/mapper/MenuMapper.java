package com.njbandou.web.mapper;

import com.njbandou.web.entity.QMenuEntity;
import com.njbandou.web.vo.item.MenuResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MenuMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QMenuEntity menu = QMenuEntity.menuEntity;

    public QueryResults<MenuResult> getAll() {
        QBean<MenuResult> columns = Projections.bean(MenuResult.class,
                menu.pkId,
                menu.parentId,
                menu.title);

        return jpaQueryFactory.select(columns)
                .from(menu)
                .fetchResults();
    }

    public Integer findParentIdByPkId(Integer pkId) {
        return jpaQueryFactory.selectDistinct(menu.parentId)
                .from(menu)
                .where(menu.pkId.eq(pkId))
                .fetchOne();
    }

}
