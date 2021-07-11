package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.*;
import io.swagger.annotations.*;
import lombok.*;

import java.math.*;
import java.sql.*;

@Data
@ApiModel(value = "考试表单条数据dto", description = "从客户端，由用户传来的数据封装在此dto中")
public class ExaminationDTO {

    @ApiModelProperty(name = "pk_id", value = "考试表主键")
    private Integer pkId;
    @ApiModelProperty(name = " paperId", value = "试卷id")
    private Integer paperId;
    @ApiModelProperty(name = " time", value = "int(11)-时长(分钟)")
    private Integer time = 0;
    @ApiModelProperty(name = " questionLimit", value = "int(11)-按题限时(0:是 1:,否)")
    private Integer questionLimit = 1;
    @ApiModelProperty(name = " passingGrade", value = "int(11)-,及格分")
    private Integer passingGrade = 0;
    @ApiModelProperty(name = " brak_barrier",value = "int(11)-闯关模式(0:是 1:否)")
    private Integer brakBarrier = 1;
    @ApiModelProperty(name = " examination_time_use",value = "int(11)-启用考试时间(0:是 1:否)")
    private Integer examinationTimeUse = 1;
    @ApiModelProperty(name = " examination_time-varchar(500)",
            value = "考试时间(JSONString)(\"" +
                    "{\"startTime\":\"2021-3-8 12:00\"," +
                    "\"endTime\":\"2021-3-8 13:00\"," +
                    "\"viewResult\":1," +
                    "\"isLate\":0," +
                    "\"lateTime\":10," +
                    "\"isSubmit\":0," +
                    "\"submitTime\":10}\")")
    private String examinationTime = "{}";


    @ApiModelProperty(name = " multipleExamination",value = "int(11)- 多次考试(0:是 1:,否)")
    private Integer multipleExamination = 1;
    @ApiModelProperty(name = " multipleExaminationTime",value = "int(11)- 多次考试次数")
    private Integer multipleExaminationTime = 0;
    @ApiModelProperty(name = " chargeExamination",value = "int(11)-收费考试(0:是 1: 否)")
    private Integer chargeExamination = 1;
    @ApiModelProperty(name = " charge_examination_price-decimal(10,2)- 收费考试价格")
    private BigDecimal chargeExaminationPrice = new BigDecimal(0);
    @ApiModelProperty(name = " sign",value = "int(11)-是否需要签名(0:是 1: 否)")
    private Integer sign = 1;
    @ApiModelProperty(name = " scoreDisplay",value = "int(11)-是否显示分值(0:是 1: 否)")
    private Integer scoreDisplay = 1;
    @ApiModelProperty(name = " questionTypeDisplay",value = "int(11)-是否显示题型(0:是 1: 否)")
    private Integer questionTypeDisplay = 1;
    @ApiModelProperty(name = " randomOrder-varchar(16)-随机顺序(0:题目 1:选项)多选时, 隔开")
    private String randomOrder = "";
    @ApiModelProperty(name = " handAfterFinish",value = "int(11)-全部答完才能交卷(0:是 1: 否)")
    private Integer handAfterFinish = 1;
    @ApiModelProperty(name = " skipQuestion",value = "int(11)-单选和判断自动跳题(0:是 1: 否)")
    private Integer skipQuestion = 1;
    @ApiModelProperty(name = " preventionCheat",value = "int(11)-防作弊(0:是 1: 否)")
    private Integer preventionCheat = 1;
    @ApiModelProperty(name = " preventionCheatTime",value = "int(11)- 防作弊次数")
    private Integer preventionCheatTime = 1;
    @ApiModelProperty(name = " dominateScreen",value = "int(11)-霸屏考试(0:是 1: 否)")
    private Integer dominateScreen = 1;
    @ApiModelProperty(name = " calculator",value = "int(11)-科学计算器(0:是 1: 否)")
    private Integer calculator = 1;
    @ApiModelProperty(name = " formula",value = "int(11)-公式编辑器(0:是 1: 否)")
    private Integer formula = 1;
    @ApiModelProperty(name = " resultsView",value = "int(11)-成绩查看(0:不允许 1:允许 2:可查对错 3: 可查答案)")
    private Integer resultsView = 0;
    @ApiModelProperty(name = " examinationCommentsUse",value = "int(11)-启用考试评语(0:是 1: 否)")
    private Integer examinationCommentsUse = 1;
    @ApiModelProperty(name = " examinationComments-varchar(500)-考试评语( JSONString)")
    private String examinationComments = "[]";
    @ApiModelProperty(name = " wrongQuestions",value = "int(11)-判分后错题加入错题集(0:是 1: 否)")
    private Integer wrongQuestions = 1;
    @ApiModelProperty(name = " selfGrade",value = "int(11)-主观题自评分(0:是 1: 否)")
    private Integer selfGrade = 1;


    private ExaminationEntity set(ExaminationEntity examination) {
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

    public ExaminationEntity insert(Integer paperId) {
        ExaminationEntity examination = set(new ExaminationEntity());
        examination.setPaperId(paperId);
        examination.setDeleteFlag(0);
        examination.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return examination;
    }

    public ExaminationEntity update(ExaminationEntity examination) {
        return set(examination);
    }
}
