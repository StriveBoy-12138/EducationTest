package com.njbandou.web.mapper;

import com.njbandou.web.dto.search.SearchExaminationPaperDTO;
import com.njbandou.web.dto.search.SearchUserDTO;
import com.njbandou.web.entity.*;
import com.njbandou.web.vo.item.ExaminationPaperResult;
import com.njbandou.web.vo.item.ExaminationPaperResult;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class ExaminationPaperMapper {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    
    private static QExaminationPaperEntity examinationPaper = QExaminationPaperEntity.examinationPaperEntity;
    private static QUserEntity user = QUserEntity.userEntity;
    private static QOrganizationEntity organization = QOrganizationEntity.organizationEntity;

    public QueryResults<ExaminationPaperResult> selectByPage(SearchExaminationPaperDTO searchExaminationPaperDTO) {
        QBean<ExaminationPaperResult> columns = Projections.bean(ExaminationPaperResult.class,
                examinationPaper.pkId,
                examinationPaper.userId,
                user.account.as("userAccount"),
                user.nickname.as("userName"),
                user.organizationId,
                organization.name.as("organizationName"),
                examinationPaper.userInfo,
                examinationPaper.paperId,
                examinationPaper.questions,
                examinationPaper.userScore,
                examinationPaper.finishTime,
                examinationPaper.useTime,
                examinationPaper.isJudge,
                examinationPaper.isMark,
                examinationPaper.status);

        JPAQuery<ExaminationPaperResult> query = jpaQueryFactory.select(columns)
                .from(examinationPaper)
                .where(examinationPaper.paperId.eq(searchExaminationPaperDTO.getPaperId()))
                .leftJoin(user).on(examinationPaper.userId.eq(user.pkId))
                .leftJoin(organization).on(user.organizationId.eq(organization.pkId));

        if (!searchExaminationPaperDTO.getStatus().equals(-1)){
            query.where(examinationPaper.status.eq(searchExaminationPaperDTO.getStatus()));
        }
        if (!searchExaminationPaperDTO.getIsJudge().equals(-1)){
            query.where(examinationPaper.isJudge.eq(searchExaminationPaperDTO.getIsJudge()));
        }
        if (!searchExaminationPaperDTO.getIsMark().equals(-1)){
            query.where(examinationPaper.isMark.eq(searchExaminationPaperDTO.getIsMark()));
        }
        if (StringUtils.isNotBlank(searchExaminationPaperDTO.getStartTime())){
            query.where(examinationPaper.finishTime.after(Timestamp.valueOf(searchExaminationPaperDTO.getStartTime())));
        }
        if (StringUtils.isNotBlank(searchExaminationPaperDTO.getEndTime())){
            query.where(examinationPaper.finishTime.before(Timestamp.valueOf(searchExaminationPaperDTO.getEndTime())));
        }
        if (StringUtils.isNotBlank(searchExaminationPaperDTO.getUserInfo())){
            query.where(examinationPaper.userInfo.contains(searchExaminationPaperDTO.getUserInfo()));
        }
        if (StringUtils.isNotBlank(searchExaminationPaperDTO.getKeyWord())){
            query.where(user.account.contains(searchExaminationPaperDTO.getKeyWord()).or(user.nickname.contains(searchExaminationPaperDTO.getKeyWord())));
        }
        if (StringUtils.isNotBlank(searchExaminationPaperDTO.getFinishTime())){
            if (searchExaminationPaperDTO.getFinishTime().equals("down")){
                query.orderBy(examinationPaper.finishTime.desc());
            }else if (searchExaminationPaperDTO.getFinishTime().equals("up")){
                query.orderBy(examinationPaper.finishTime.asc());
            }
        }
        if (StringUtils.isNotBlank(searchExaminationPaperDTO.getScores())){
            if (searchExaminationPaperDTO.getScores().equals("down")){
                query.orderBy(examinationPaper.userScore.desc());
            }else if (searchExaminationPaperDTO.getScores().equals("up")){
                query.orderBy(examinationPaper.userScore.asc());
            }
        }

        return query.offset(searchExaminationPaperDTO.getPage())
                .limit(searchExaminationPaperDTO.getPageSize())
                .fetchResults();
    }
}
