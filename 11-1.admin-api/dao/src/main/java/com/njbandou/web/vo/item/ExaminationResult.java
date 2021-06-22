package com.njbandou.web.vo.item;

import com.njbandou.web.entity.ExaminationEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExaminationResult {
    private Integer pkId;
    private Integer paperId;
    private Integer time;
    private Boolean questionLimit;
    private Integer passingGrade;
    private Boolean brakBarrier;
    private Boolean examinationTimeUse;
    private String examinationTime;
    private Boolean multipleExamination;
    private Integer multipleExaminationTime;
    private Boolean chargeExamination;
    private BigDecimal chargeExaminationPrice;
    private Boolean sign;
    private Boolean scoreDisplay;
    private Boolean questionTypeDisplay;
    private String randomOrder;
    private Boolean handAfterFinish;
    private Boolean skipQuestion;
    private Boolean preventionCheat;
    private Integer preventionCheatTime;
    private Boolean dominateScreen;
    private Boolean calculator;
    private Boolean formula;
    private Integer resultsView;
    private Boolean examinationCommentsUse;
    private String examinationComments;
    private Boolean wrongQuestions;
    private Boolean selfGrade;

    public ExaminationResult(ExaminationEntity examination) {
        this.pkId = examination.getPkId();
        this.paperId = examination.getPaperId();
        this.time = examination.getTime();
        this.questionLimit = examination.getQuestionLimit().equals(0);
        this.passingGrade = examination.getPassingGrade();
        this.brakBarrier = examination.getBrakBarrier().equals(0);
        this.examinationTimeUse = examination.getExaminationTimeUse().equals(0);
        this.examinationTime = examination.getExaminationTime();
        this.multipleExamination = examination.getMultipleExamination().equals(0);
        this.multipleExaminationTime = examination.getMultipleExaminationTime();
        this.chargeExamination = examination.getChargeExamination().equals(0);
        this.chargeExaminationPrice = examination.getChargeExaminationPrice();
        this.sign = examination.getSign().equals(0);
        this.scoreDisplay = examination.getScoreDisplay().equals(0);
        this.questionTypeDisplay = examination.getQuestionTypeDisplay().equals(0);
        this.randomOrder = examination.getRandomOrder();
        this.handAfterFinish = examination.getHandAfterFinish().equals(0);
        this.skipQuestion = examination.getSkipQuestion().equals(0);
        this.preventionCheat = examination.getPreventionCheat().equals(0);
        this.preventionCheatTime = examination.getPreventionCheatTime();
        this.dominateScreen = examination.getDominateScreen().equals(0);
        this.calculator = examination.getCalculator().equals(0);
        this.formula = examination.getFormula().equals(0);
        this.resultsView = examination.getResultsView();
        this.examinationCommentsUse = examination.getExaminationCommentsUse().equals(0);
        this.examinationComments = examination.getExaminationComments();
        this.wrongQuestions = examination.getWrongQuestions().equals(0);
        this.selfGrade = examination.getSelfGrade().equals(0);
    }
}
