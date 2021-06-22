package com.njbandou.web.mapper;

import com.njbandou.web.entity.MenuEntity;
import com.njbandou.web.entity.QMenuEntity;
import com.njbandou.web.entity.QRoleMenuEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleMenuMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QMenuEntity menuEntity = QMenuEntity.menuEntity;
    private static QRoleMenuEntity roleMenuEntity = QRoleMenuEntity.roleMenuEntity;

    public List<MenuEntity> findByRoleIds(List<Integer> roleIds) {
        return jpaQueryFactory.select(menuEntity)
                .from(menuEntity, roleMenuEntity)
                .where(roleMenuEntity.menuId.eq(menuEntity.pkId)
                        .and(roleMenuEntity.roleId.in(roleIds)))
                .fetch();
    }
}
