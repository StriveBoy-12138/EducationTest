package com.njbandou.web.mapper;

import com.njbandou.web.dto.search.SearchUserDTO;
import com.njbandou.web.entity.*;
import com.njbandou.web.vo.item.InviteUserResult;
import com.njbandou.web.vo.item.UserResult;
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
public class UserMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    private static QUserEntity user = QUserEntity.userEntity;
    private static QTitleEntity title = QTitleEntity.titleEntity;
    private static QOrganizationEntity organization = QOrganizationEntity.organizationEntity;
    private static QRoleEntity role = QRoleEntity.roleEntity;
    private static QUserRoleEntity userRole = QUserRoleEntity.userRoleEntity;
    private static QMenuEntity menuEntity = QMenuEntity.menuEntity;
    private static QRoleMenuEntity roleMenu = QRoleMenuEntity.roleMenuEntity;

    public List<String> selectUserAccessInfoByPkId(Integer userId) {
        return jpaQueryFactory.select(menuEntity.perms)
                .from(userRole)
                .leftJoin(roleMenu).on(userRole.roleId.eq(roleMenu.roleId))
                .leftJoin(menuEntity).on(menuEntity.pkId.eq(roleMenu.menuId))
                .where(userRole.userId.eq(userId))
                .fetch();
    }

    public QueryResults<UserResult> selectByPage(SearchUserDTO searchUserDTO) {
        QBean<UserResult> columns = Projections.bean(UserResult.class,
                user.pkId,
                user.title,
                title.name.as("titleName"),
                user.type,
                user.account,
                user.avatar,
                user.nickname,
                user.phone,
                user.organizationId,
                organization.name.as("organizationName"),
                userRole.roleId,
                role.name.as("roleName"),
                user.isEnabled,
                user.otherInfo,
                user.createTime,
                user.updateTime);

        JPAQuery<UserResult> query = jpaQueryFactory.select(columns)
                .from(user)
                .leftJoin(organization).on(user.organizationId.eq(organization.pkId))
                .leftJoin(title).on(user.title.eq(title.pkId))
                .leftJoin(userRole).on(user.pkId.eq(userRole.userId))
                .leftJoin(role).on(userRole.roleId.eq(role.pkId));

        if (!searchUserDTO.getTitle().equals(0)){
            query.where(user.title.eq(searchUserDTO.getTitle()));
        }
        if (!searchUserDTO.getIsEnabled().equals(-1)){
            query.where(user.isEnabled.eq(searchUserDTO.getIsEnabled()));
        }
        if (searchUserDTO.getOrganizationIds() != null){
            query.where(user.organizationId.in(searchUserDTO.getOrganizationIds()));
        }
        if (StringUtils.isNotBlank(searchUserDTO.getKeyword())){
            query.where(user.account.contains(searchUserDTO.getKeyword()).or(user.nickname.contains(searchUserDTO.getKeyword())));
        }
        if (StringUtils.isNotBlank(searchUserDTO.getUserAccount())){
            query.where(user.account.contains(searchUserDTO.getUserAccount()));
        }
        if (StringUtils.isNotBlank(searchUserDTO.getUserName())){
            query.where(user.nickname.contains(searchUserDTO.getUserName()));
        }
        if (searchUserDTO.getExceptUserIds() != null && searchUserDTO.getExceptUserIds().length != 0){
            query.where(user.pkId.notIn(searchUserDTO.getExceptUserIds()));
        }

        return query.orderBy(user.pkId.desc())
                .offset(searchUserDTO.getPage())
                .limit(searchUserDTO.getPageSize())
                .fetchResults();
    }

    public QueryResults<InviteUserResult> selectInviteUserByPage(SearchUserDTO searchUserDTO) {
        QBean<InviteUserResult> columns = Projections.bean(InviteUserResult.class,
                user.pkId,
                user.account,
                user.nickname);

        JPAQuery<InviteUserResult> query = jpaQueryFactory.select(columns)
                .from(user)
                .where(user.pkId.in(searchUserDTO.getUserIds()));

        if (StringUtils.isNotBlank(searchUserDTO.getUserAccount())){
            query.where(user.account.contains(searchUserDTO.getUserAccount()));
        }
        if (StringUtils.isNotBlank(searchUserDTO.getUserName())){
            query.where(user.nickname.contains(searchUserDTO.getUserName()));
        }

        return query.orderBy(user.pkId.desc())
                .offset(searchUserDTO.getPage())
                .limit(searchUserDTO.getPageSize())
                .fetchResults();
    }
}
