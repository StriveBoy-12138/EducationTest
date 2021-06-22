package com.njbandou.web.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_examination", schema = "db_examination", catalog = "")
@Where(clause = "delete_flag = 0")
@SQLDelete(sql = "update t_examination set delete_flag = 1 where pk_id = ?")
public class ExaminationEntity {
    private Integer pkId;
    private Integer paperId;
    private Integer time;
    private Integer questionLimit;
    private Integer passingGrade;
    private Integer brakBarrier;
    private Integer examinationTimeUse;
    private String examinationTime;
    private Integer multipleExamination;
    private Integer multipleExaminationTime;
    private Integer chargeExamination;
    private BigDecimal chargeExaminationPrice;
    private Integer sign;
    private Integer scoreDisplay;
    private Integer questionTypeDisplay;
    private String randomOrder;
    private Integer handAfterFinish;
    private Integer skipQuestion;
    private Integer preventionCheat;
    private Integer preventionCheatTime;
    private Integer dominateScreen;
    private Integer calculator;
    private Integer formula;
    private Integer resultsView;
    private Integer examinationCommentsUse;
    private String examinationComments;
    private Integer wrongQuestions;
    private Integer selfGrade;
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
    @Column(name = "time")
    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Basic
    @Column(name = "question_limit")
    public Integer getQuestionLimit() {
        return questionLimit;
    }

    public void setQuestionLimit(Integer questionLimit) {
        this.questionLimit = questionLimit;
    }

    @Basic
    @Column(name = "passing_grade")
    public Integer getPassingGrade() {
        return passingGrade;
    }

    public void setPassingGrade(Integer passingGrade) {
        this.passingGrade = passingGrade;
    }

    @Basic
    @Column(name = "brak_barrier")
    public Integer getBrakBarrier() {
        return brakBarrier;
    }

    public void setBrakBarrier(Integer brakBarrier) {
        this.brakBarrier = brakBarrier;
    }

    @Basic
    @Column(name = "examination_time_use")
    public Integer getExaminationTimeUse() {
        return examinationTimeUse;
    }

    public void setExaminationTimeUse(Integer examinationTimeUse) {
        this.examinationTimeUse = examinationTimeUse;
    }

    @Basic
    @Column(name = "examination_time")
    public String getExaminationTime() {
        return examinationTime;
    }

    public void setExaminationTime(String examinationTime) {
        this.examinationTime = examinationTime;
    }

    @Basic
    @Column(name = "multiple_examination")
    public Integer getMultipleExamination() {
        return multipleExamination;
    }

    public void setMultipleExamination(Integer multipleExamination) {
        this.multipleExamination = multipleExamination;
    }

    @Basic
    @Column(name = "multiple_examination_time")
    public Integer getMultipleExaminationTime() {
        return multipleExaminationTime;
    }

    public void setMultipleExaminationTime(Integer multipleExaminationTime) {
        this.multipleExaminationTime = multipleExaminationTime;
    }

    @Basic
    @Column(name = "charge_examination")
    public Integer getChargeExamination() {
        return chargeExamination;
    }

    public void setChargeExamination(Integer chargeExamination) {
        this.chargeExamination = chargeExamination;
    }

    @Basic
    @Column(name = "charge_examination_price")
    public BigDecimal getChargeExaminationPrice() {
        return chargeExaminationPrice;
    }

    public void setChargeExaminationPrice(BigDecimal chargeExaminationPrice) {
        this.chargeExaminationPrice = chargeExaminationPrice;
    }

    @Basic
    @Column(name = "sign")
    public Integer getSign() {
        return sign;
    }

    public void setSign(Integer sign) {
        this.sign = sign;
    }

    @Basic
    @Column(name = "score_display")
    public Integer getScoreDisplay() {
        return scoreDisplay;
    }

    public void setScoreDisplay(Integer scoreDisplay) {
        this.scoreDisplay = scoreDisplay;
    }

    @Basic
    @Column(name = "question_type_display")
    public Integer getQuestionTypeDisplay() {
        return questionTypeDisplay;
    }

    public void setQuestionTypeDisplay(Integer questionTypeDisplay) {
        this.questionTypeDisplay = questionTypeDisplay;
    }

    @Basic
    @Column(name = "random_order")
    public String getRandomOrder() {
        return randomOrder;
    }

    public void setRandomOrder(String randomOrder) {
        this.randomOrder = randomOrder;
    }

    @Basic
    @Column(name = "hand_after_finish")
    public Integer getHandAfterFinish() {
        return handAfterFinish;
    }

    public void setHandAfterFinish(Integer handAfterFinish) {
        this.handAfterFinish = handAfterFinish;
    }

    @Basic
    @Column(name = "skip_question")
    public Integer getSkipQuestion() {
        return skipQuestion;
    }

    public void setSkipQuestion(Integer skipQuestion) {
        this.skipQuestion = skipQuestion;
    }

    @Basic
    @Column(name = "prevention_cheat")
    public Integer getPreventionCheat() {
        return preventionCheat;
    }

    public void setPreventionCheat(Integer preventionCheat) {
        this.preventionCheat = preventionCheat;
    }

    @Basic
    @Column(name = "prevention_cheat_time")
    public Integer getPreventionCheatTime() {
        return preventionCheatTime;
    }

    public void setPreventionCheatTime(Integer preventionCheatTime) {
        this.preventionCheatTime = preventionCheatTime;
    }

    @Basic
    @Column(name = "dominate_screen")
    public Integer getDominateScreen() {
        return dominateScreen;
    }

    public void setDominateScreen(Integer dominateScreen) {
        this.dominateScreen = dominateScreen;
    }

    @Basic
    @Column(name = "calculator")
    public Integer getCalculator() {
        return calculator;
    }

    public void setCalculator(Integer calculator) {
        this.calculator = calculator;
    }

    @Basic
    @Column(name = "formula")
    public Integer getFormula() {
        return formula;
    }

    public void setFormula(Integer formula) {
        this.formula = formula;
    }

    @Basic
    @Column(name = "results_view")
    public Integer getResultsView() {
        return resultsView;
    }

    public void setResultsView(Integer resultsView) {
        this.resultsView = resultsView;
    }

    @Basic
    @Column(name = "examination_comments_use")
    public Integer getExaminationCommentsUse() {
        return examinationCommentsUse;
    }

    public void setExaminationCommentsUse(Integer examinationCommentsUse) {
        this.examinationCommentsUse = examinationCommentsUse;
    }

    @Basic
    @Column(name = "examination_comments")
    public String getExaminationComments() {
        return examinationComments;
    }

    public void setExaminationComments(String examinationComments) {
        this.examinationComments = examinationComments;
    }

    @Basic
    @Column(name = "wrong_questions")
    public Integer getWrongQuestions() {
        return wrongQuestions;
    }

    public void setWrongQuestions(Integer wrongQuestions) {
        this.wrongQuestions = wrongQuestions;
    }

    @Basic
    @Column(name = "self_grade")
    public Integer getSelfGrade() {
        return selfGrade;
    }

    public void setSelfGrade(Integer selfGrade) {
        this.selfGrade = selfGrade;
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
        ExaminationEntity that = (ExaminationEntity) o;
        return pkId == that.pkId &&
                paperId == that.paperId &&
                time == that.time &&
                questionLimit == that.questionLimit &&
                passingGrade == that.passingGrade &&
                brakBarrier == that.brakBarrier &&
                examinationTimeUse == that.examinationTimeUse &&
                multipleExamination == that.multipleExamination &&
                multipleExaminationTime == that.multipleExaminationTime &&
                chargeExamination == that.chargeExamination &&
                sign == that.sign &&
                scoreDisplay == that.scoreDisplay &&
                questionTypeDisplay == that.questionTypeDisplay &&
                handAfterFinish == that.handAfterFinish &&
                skipQuestion == that.skipQuestion &&
                preventionCheat == that.preventionCheat &&
                preventionCheatTime == that.preventionCheatTime &&
                dominateScreen == that.dominateScreen &&
                calculator == that.calculator &&
                formula == that.formula &&
                resultsView == that.resultsView &&
                examinationCommentsUse == that.examinationCommentsUse &&
                wrongQuestions == that.wrongQuestions &&
                selfGrade == that.selfGrade &&
                deleteFlag == that.deleteFlag &&
                Objects.equals(examinationTime, that.examinationTime) &&
                Objects.equals(chargeExaminationPrice, that.chargeExaminationPrice) &&
                Objects.equals(randomOrder, that.randomOrder) &&
                Objects.equals(examinationComments, that.examinationComments) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkId, paperId, time, questionLimit, passingGrade, brakBarrier, examinationTimeUse, examinationTime, multipleExamination, multipleExaminationTime, chargeExamination, chargeExaminationPrice, sign, scoreDisplay, questionTypeDisplay, randomOrder, handAfterFinish, skipQuestion, preventionCheat, preventionCheatTime, dominateScreen, calculator, formula, resultsView, examinationCommentsUse, examinationComments, wrongQuestions, selfGrade, deleteFlag, createTime, updateTime);
    }
}
