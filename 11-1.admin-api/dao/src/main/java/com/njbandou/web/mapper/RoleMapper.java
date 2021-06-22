package com.njbandou.web.mapper;

import com.njbandou.web.dto.search.SearchRoleDTO;
import com.njbandou.web.entity.QRoleEntity;
import com.njbandou.web.entity.QUserRoleEntity;
import com.njbandou.web.entity.RoleEntity;
import com.njbandou.web.vo.item.RoleListResult;
import com.njbandou.web.vo.item.RoleResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QRoleEntity role = QRoleEntity.roleEntity;
    private static QUserRoleEntity userRole = QUserRoleEntity.userRoleEntity;

    public QueryResults<RoleResult> selectByPage(SearchRoleDTO searchRoleDTO) {
        QBean<RoleResult> columns = Projections.bean(RoleResult.class,
                role.pkId,
                role.name,
                role.des,
                role.createTime);

        JPAQuery<RoleResult> query = jpaQueryFactory.select(columns)
                .from(role);

        if (StringUtils.isNotBlank(searchRoleDTO.getName())){
            query.where(role.name.like("%" + searchRoleDTO.getName() + "%"));
        }

        return query.offset(searchRoleDTO.getPage())
                .limit(searchRoleDTO.getPageSize())
                .fetchResults();
    }

    public QueryResults<RoleListResult> selectAll() {
        QBean<RoleListResult> columns = Projections.bean(RoleListResult.class,
                role.pkId,
                role.name);

        JPAQuery<RoleListResult> query = jpaQueryFactory.select(columns).from(role);

        return query.orderBy(role.pkId.asc())
                .fetchResults();
    }

    public List<RoleEntity> findByUserId(Integer userId) {
        return jpaQueryFactory.selectDistinct(role)
                .from(role, userRole)
                .where(userRole.userId.eq(userId)
                .and(role.pkId.eq(userRole.roleId)))
                .fetch();
    }
}
