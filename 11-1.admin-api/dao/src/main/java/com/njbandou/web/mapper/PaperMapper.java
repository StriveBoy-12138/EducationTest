package com.njbandou.web.mapper;

import com.njbandou.web.dto.search.SearchPaperDTO;
import com.njbandou.web.entity.QPaperEntity;
import com.njbandou.web.entity.QPaperTypeEntity;
import com.njbandou.web.entity.QUserEntity;
import com.njbandou.web.vo.item.PaperResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PaperMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static QPaperEntity paper = QPaperEntity.paperEntity;
    private static QPaperTypeEntity paperType = QPaperTypeEntity.paperTypeEntity;
    private static QUserEntity user = QUserEntity.userEntity;

    public QueryResults<PaperResult> selectByPage(SearchPaperDTO searchPaperDTO) {
        QBean<PaperResult> columns = Projections.bean(PaperResult.class,
                paper.pkId,
                paper.typeId,
                paperType.name.as("typeName"),
                paper.title,
                paper.des,
                paper.isOpen,
                paper.isFixed,
                paper.questions,
                paper.questionRule,
                paper.status,
                paper.placeFile,
                paper.authorId,
                user.nickname.as("authorName"),
                paper.inviteUser,
                paper.inviteTitle,
                paper.inviteOrganization,
                paper.inviteAll,
                paper.updateTime,
                paper.createTime);

        JPAQuery<PaperResult> query = jpaQueryFactory.select(columns).from(paper)
                .where(paper.placeFile.eq(searchPaperDTO.getPlaceFile()))
                .leftJoin(paperType).on(paper.typeId.eq(paperType.pkId))
                .leftJoin(user).on(paper.authorId.eq(user.pkId));

        if (!searchPaperDTO.getTypeId().equals(0)){
            query.where(paper.typeId.eq(searchPaperDTO.getTypeId()));
        }
        if (!searchPaperDTO.getStatus().equals(-1)){
            query.where(paper.status.eq(searchPaperDTO.getStatus()));
        }
        if (!searchPaperDTO.getIsOpen().equals(-1)){
            query.where(paper.isOpen.eq(searchPaperDTO.getIsOpen()));
        }
        if (!searchPaperDTO.getIsFixed().equals(-1)){
            query.where(paper.isFixed.eq(searchPaperDTO.getIsFixed()));
        }
        if (StringUtils.isNotBlank(searchPaperDTO.getTitle())){
            query.where(paper.title.contains(searchPaperDTO.getTitle()));
        }

        return query.orderBy(paper.pkId.desc())
                .offset(searchPaperDTO.getPage())
                .limit(searchPaperDTO.getPageSize())
                .fetchResults();
    }
}
