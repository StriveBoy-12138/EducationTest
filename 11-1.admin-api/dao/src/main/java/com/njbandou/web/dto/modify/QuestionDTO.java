package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.QuestionEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

@ApiModel(value = "题目对象DTO", description = "从客户端，由用户传入的数据封装在此DTO中")
@Data
public class QuestionDTO {

    @ApiModelProperty(value = "题目主键", name = "pkId")
    private Integer pkId;
    @ApiModelProperty(value = "题库id", name = "bankId")
    private Integer bankId;
    @ApiModelProperty(value = "题目id", name = "pkId")
    private Integer questionId;
    @ApiModelProperty(value = "题目分类", name = "type")
    private Integer type;
    @ApiModelProperty(value = "题目信息", name = "title")
    private String title;
    @ApiModelProperty(value = "知识点id(,隔开)", name = "knowledgePoint")
    private String knowledgePoint;
    @ApiModelProperty(value = "音频名称", name = "audioName")
    private String audioName;
    @ApiModelProperty(value = "音频", name = "audio")
    private String audio;
    @ApiModelProperty(value = "视频名称", name = "videoName")
    private String videoName;
    @ApiModelProperty(value = "视频", name = "video")
    private String video;
    @ApiModelProperty(value = "文档名称(,隔开)", name = "fileName")
    private String fileName;
    @ApiModelProperty(value = "文档(,隔开)", name = "file")
    private String file;
    @ApiModelProperty(value = "解析", name = "analysis")
    private String analysis;
    @ApiModelProperty(value = "解析音频名称", name = "analysisAudioName")
    private String analysisAudioName;
    @ApiModelProperty(value = "解析音频", name = "analysisAudio")
    private String analysisAudio;
    @ApiModelProperty(value = "解析视频名称", name = "analysisVideoName")
    private String analysisVideoName;
    @ApiModelProperty(value = "解析视频", name = "analysisVideo")
    private String analysisVideo;
    @ApiModelProperty(value = "答案(JSONString)", name = "answer")
    private String answer;
    @ApiModelProperty(value = "难度(0:高 1:中 2:低)", name = "difficulty")
    private Integer difficulty;


    private QuestionEntity set(QuestionEntity question){
        question.setTitle(title);
        question.setType(type);
        question.setKnowledgePoint(knowledgePoint);
        question.setAudioName(audioName);
        question.setAudio(audio);
        question.setVideoName(videoName);
        question.setVideo(video);
        question.setFileName(fileName);
        question.setFile(file);
        question.setAnalysis(analysis);
        question.setAnalysisAudioName(analysisAudioName);
        question.setAnalysisAudio(analysisAudio);
        question.setAnalysisVideoName(analysisVideoName);
        question.setAnalysisVideo(analysisVideo);
        question.setAnswer(answer);
        question.setDifficulty(difficulty);
        question.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return question;
    }

    public QuestionEntity insert(){
        QuestionEntity question = set(new QuestionEntity());
        question.setBankId(bankId);
        question.setQuestionId(questionId);
        question.setDeleteFlag(0);
        question.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return question;
    }

    public QuestionEntity update(QuestionEntity question){
        return set(question);
    }

    public QuestionEntity setQuestionKnowledgePoint(QuestionEntity question){
        question.setKnowledgePoint(knowledgePoint);
        question.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return question;
    }
}
