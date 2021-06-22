package com.njbandou.web.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_examination_paper", schema = "db_examination", catalog = "")
@Where(clause = "delete_flag = 0")
@SQLDelete(sql = "update t_examination_paper set delete_flag = 1 where pk_id = ?")
public class ExaminationPaperEntity {
    private Integer pkId;
    private Integer userId;
    private String userInfo;
    private Integer paperId;
    private String questions;
    private Double userScore;
    private Timestamp finishTime;
    private Integer useTime;
    private Integer isJudge;
    private Integer isMark;
    private Integer status;
    private Integer deleteFlag;
    private Timestamp createTime;
    private Timestamp updateTime;

    @Id
    @Column(name = "pk_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getPkId() {
        return pkId;
    }

    public void setPkId(Integer pkId) {
        this.pkId = pkId;
    }

    @Basic
    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "user_info")
    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    @Basic
    @Column(name = "paper_id")
    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    @Basic
    @Column(name = "questions")
    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    @Basic
    @Column(name = "user_score")
    public Double getUserScore() {
        return userScore;
    }

    public void setUserScore(Double userScore) {
        this.userScore = userScore;
    }

    @Basic
    @Column(name = "finish_time")
    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    @Basic
    @Column(name = "use_time")
    public Integer getUseTime() {
        return useTime;
    }

    public void setUseTime(Integer useTime) {
        this.useTime = useTime;
    }

    @Basic
    @Column(name = "is_judge")
    public Integer getIsJudge() {
        return isJudge;
    }

    public void setIsJudge(Integer isJudge) {
        this.isJudge = isJudge;
    }

    @Basic
    @Column(name = "is_mark")
    public Integer getIsMark() {
        return isMark;
    }

    public void setIsMark(Integer isMark) {
        this.isMark = isMark;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "delete_flag")
    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExaminationPaperEntity that = (ExaminationPaperEntity) o;
        return pkId == that.pkId &&
                userId == that.userId &&
                paperId == that.paperId &&
                Double.compare(that.userScore, userScore) == 0 &&
                useTime == that.useTime &&
                isJudge == that.isJudge &&
                isMark == that.isMark &&
                status == that.status &&
                deleteFlag == that.deleteFlag &&
                Objects.equals(userInfo, that.userInfo) &&
                Objects.equals(questions, that.questions) &&
                Objects.equals(finishTime, that.finishTime) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkId, userId, userInfo, paperId, questions, userScore, finishTime, useTime, isJudge, isMark, status, deleteFlag, createTime, updateTime);
    }
}
