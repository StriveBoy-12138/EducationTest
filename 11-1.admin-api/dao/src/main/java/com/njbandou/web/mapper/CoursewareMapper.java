package com.njbandou.web.mapper;

import com.njbandou.web.dto.search.SearchCoursewareDTO;
import com.njbandou.web.entity.QCoursewareEntity;
import com.njbandou.web.entity.QCoursewareTypeEntity;
import com.njbandou.web.entity.QUserEntity;
import com.njbandou.web.vo.item.CoursewareResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CoursewareMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QCoursewareEntity coursewareEntity = QCoursewareEntity.coursewareEntity;
    private static QCoursewareTypeEntity coursewareType = QCoursewareTypeEntity.coursewareTypeEntity;
    private static QUserEntity user = QUserEntity.userEntity;

    public QueryResults<CoursewareResult> selectByPage(SearchCoursewareDTO searchCoursewareDTO) {
        QBean<CoursewareResult> columns = Projections.bean(CoursewareResult.class,
                coursewareEntity.pkId,
                coursewareEntity.typeId,
                coursewareType.name.as("typeName"),
                coursewareEntity.title,
                coursewareEntity.studyTime,
                coursewareEntity.introduction,
                coursewareEntity.coursewareType,
                coursewareEntity.size,
                coursewareEntity.content,
                coursewareEntity.authorId,
                user.nickname.as("authorName"),
                coursewareEntity.createTime,
                coursewareEntity.updateTime);

        JPAQuery<CoursewareResult> query = jpaQueryFactory.select(columns).from(coursewareEntity)
                .leftJoin(coursewareType).on(coursewareEntity.typeId.eq(coursewareType.pkId))
                .leftJoin(user).on(coursewareEntity.authorId.eq(user.pkId));

        if (!searchCoursewareDTO.getTypeId().equals(0)){
            query.where(coursewareEntity.typeId.eq(searchCoursewareDTO.getTypeId()));
        }
        if (StringUtils.isNotBlank(searchCoursewareDTO.getTitle())){
            query.where(coursewareEntity.title.contains(searchCoursewareDTO.getTitle()));
        }

        return query.orderBy(coursewareEntity.pkId.desc())
                .offset(searchCoursewareDTO.getPage())
                .limit(searchCoursewareDTO.getPageSize())
                .fetchResults();
    }
}
