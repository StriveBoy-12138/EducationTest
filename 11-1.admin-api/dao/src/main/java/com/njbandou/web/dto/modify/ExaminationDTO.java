package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.ExaminationEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class ExaminationDTO {
    private Integer pkId;
    private Integer paperId;
    private Integer time = 0;
    private Integer questionLimit = 1;
    private Integer passingGrade = 0;
    private Integer brakBarrier = 1;
    private Integer examinationTimeUse = 1;
    private String examinationTime = "{}";
    private Integer multipleExamination = 1;
    private Integer multipleExaminationTime = 0;
    private Integer chargeExamination = 1;
    private BigDecimal chargeExaminationPrice = new BigDecimal(0);
    private Integer sign = 1;
    private Integer scoreDisplay = 1;
    private Integer questionTypeDisplay = 1;
    private String randomOrder = "";
    private Integer handAfterFinish = 1;
    private Integer skipQuestion = 1;
    private Integer preventionCheat = 1;
    private Integer preventionCheatTime = 1;
    private Integer dominateScreen = 1;
    private Integer calculator = 1;
    private Integer formula = 1;
    private Integer resultsView = 0;
    private Integer examinationCommentsUse = 1;
    private String examinationComments = "[]";
    private Integer wrongQuestions = 1;
    private Integer selfGrade = 1;

    private ExaminationEntity set(ExaminationEntity examination){
        examination.setTime(time);
        examination.setQuestionLimit(questionLimit);
        examination.setPassingGrade(passingGrade);
        examination.setBrakBarrier(brakBarrier);
        examination.setExaminationTimeUse(examinationTimeUse);
        examination.setExaminationTime(examinationTime);
        examination.setMultipleExamination(multipleExamination);
        examination.setMultipleExaminationTime(multipleExaminationTime);
        examination.setChargeExamination(chargeExamination);
        examination.setChargeExaminationPrice(chargeExaminationPrice);
        examination.setSign(sign);
        examination.setScoreDisplay(scoreDisplay);
        examination.setQuestionTypeDisplay(questionTypeDisplay);
        examination.setRandomOrder(randomOrder);
        examination.setHandAfterFinish(handAfterFinish);
        examination.setSkipQuestion(skipQuestion);
        examination.setPreventionCheat(preventionCheat);
        examination.setPreventionCheatTime(preventionCheatTime);
        examination.setDominateScreen(dominateScreen);
        examination.setCalculator(calculator);
        examination.setFormula(formula);
        examination.setResultsView(resultsView);
        examination.setExaminationCommentsUse(examinationCommentsUse);
        examination.setExaminationComments(examinationComments);
        examination.setWrongQuestions(wrongQuestions);
        examination.setSelfGrade(selfGrade);
        examination.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return examination;
    }

    public ExaminationEntity insert(Integer paperId){
        ExaminationEntity examination = set(new ExaminationEntity());
        examination.setPaperId(paperId);
        examination.setDeleteFlag(0);
        examination.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return examination;
    }

    public ExaminationEntity update(ExaminationEntity examination){
        return set(examination);
    }
}
