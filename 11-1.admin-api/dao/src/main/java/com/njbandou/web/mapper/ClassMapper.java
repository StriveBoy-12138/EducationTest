package com.njbandou.web.mapper;

import com.njbandou.web.dto.search.SearchClassDTO;
import com.njbandou.web.entity.QClassEntity;
import com.njbandou.web.entity.QClassTypeEntity;
import com.njbandou.web.entity.QPaperEntity;
import com.njbandou.web.entity.QUserEntity;
import com.njbandou.web.vo.item.ClassResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ClassMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QClassEntity classEntity = QClassEntity.classEntity;
    private static QClassTypeEntity classType = QClassTypeEntity.classTypeEntity;
    private static QUserEntity user = QUserEntity.userEntity;
    private static QPaperEntity paper = QPaperEntity.paperEntity;

    public QueryResults<ClassResult> selectByPage(SearchClassDTO searchClassDTO) {
        QBean<ClassResult> columns = Projections.bean(ClassResult.class,
                classEntity.pkId,
                classEntity.typeId,
                classType.name.as("typeName"),
                classEntity.title,
                classEntity.credit,
                classEntity.preventionCheat,
                classEntity.videoDrag,
                classEntity.creditAcquisition,
                classEntity.studyTime,
                classEntity.picture,
                classEntity.des,
                classEntity.classCharge,
                classEntity.collectNum,
                classEntity.upNum,
                classEntity.inviteUser,
                classEntity.inviteTitle,
                classEntity.inviteOrganization,
                classEntity.inviteAll,
                classEntity.paper,
                paper.title.as("paperName"),
                classEntity.status,
                classEntity.authorId,
                user.nickname.as("authorName"),
                classEntity.createTime,
                classEntity.publishTime,
                classEntity.updateTime);

        JPAQuery<ClassResult> query = jpaQueryFactory.select(columns).from(classEntity)
                .leftJoin(classType).on(classEntity.typeId.eq(classType.pkId))
                .leftJoin(user).on(classEntity.authorId.eq(user.pkId))
                .leftJoin(paper).on(classEntity.paper.eq(paper.pkId));

        if (!searchClassDTO.getTypeId().equals(0)){
            query.where(classEntity.typeId.eq(searchClassDTO.getTypeId()));
        }
        if (StringUtils.isNotBlank(searchClassDTO.getTitle())){
            query.where(classEntity.title.contains(searchClassDTO.getTitle()));
        }


        return query.orderBy(classEntity.pkId.asc())
                .offset(searchClassDTO.getPage())
                .limit(searchClassDTO.getPageSize())
                .fetchResults();
    }
}
