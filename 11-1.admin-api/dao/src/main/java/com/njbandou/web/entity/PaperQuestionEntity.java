package com.njbandou.web.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_paper_question", schema = "db_examination", catalog = "")
@Where(clause = "delete_flag = 0")
@SQLDelete(sql = "update t_paper_question set delete_flag = 1 where pk_id = ?")
public class PaperQuestionEntity {
    private Integer pkId;
    private Integer paperId;
    private String randomId;
    private Integer questionId;
    private Double score;
    private String multipleChoice;
    private String userAnswer;
    private Double userScore;
    private Integer isRight;
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
    @Column(name = "paper_id")
    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    @Basic
    @Column(name = "random_id")
    public String getRandomId() {
        return randomId;
    }

    public void setRandomId(String randomId) {
        this.randomId = randomId;
    }

    @Basic
    @Column(name = "question_id")
    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    @Basic
    @Column(name = "score")
    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Basic
    @Column(name = "multiple_choice")
    public String getMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(String multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    @Basic
    @Column(name = "user_answer")
    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
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
    @Column(name = "is_right")
    public Integer getIsRight() {
        return isRight;
    }

    public void setIsRight(Integer isRight) {
        this.isRight = isRight;
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
        PaperQuestionEntity that = (PaperQuestionEntity) o;
        return pkId == that.pkId &&
                paperId == that.paperId &&
                questionId == that.questionId &&
                Double.compare(that.score, score) == 0 &&
                Double.compare(that.userScore, userScore) == 0 &&
                isRight == that.isRight &&
                deleteFlag == that.deleteFlag &&
                Objects.equals(randomId, that.randomId) &&
                Objects.equals(multipleChoice, that.multipleChoice) &&
                Objects.equals(userAnswer, that.userAnswer) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkId, paperId, randomId, questionId, score, multipleChoice, userAnswer, userScore, isRight, deleteFlag, createTime, updateTime);
    }
}
