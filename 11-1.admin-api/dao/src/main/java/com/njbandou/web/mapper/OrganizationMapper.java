package com.njbandou.web.mapper;

import com.njbandou.web.entity.QOrganizationEntity;
import com.njbandou.web.vo.item.InviteOrganizationResult;
import com.njbandou.web.vo.item.OrganizationResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrganizationMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QOrganizationEntity organization = QOrganizationEntity.organizationEntity;

    public QueryResults<OrganizationResult> selectAll(Integer[] exceptOrganizationIds){
        QBean<OrganizationResult> columns = Projections.bean(OrganizationResult.class,
                organization.pkId,
                organization.parentId,
                organization.name.as("title"));

        JPAQuery<OrganizationResult> query = jpaQueryFactory.select(columns).from(organization);

        if (exceptOrganizationIds != null && exceptOrganizationIds.length != 0){
            query.where(organization.pkId.notIn(exceptOrganizationIds));
        }

        return query.orderBy(organization.pkId.asc())
                .fetchResults();
    }

    public QueryResults<InviteOrganizationResult> selectByIds(Integer[] organizationIds){
        QBean<InviteOrganizationResult> columns = Projections.bean(InviteOrganizationResult.class,
                organization.pkId,
                organization.name.as("title"));

        JPAQuery<InviteOrganizationResult> query = jpaQueryFactory.select(columns).from(organization)
                .where(organization.pkId.in(organizationIds));

        return query.orderBy(organization.pkId.asc())
                .fetchResults();
    }
}
