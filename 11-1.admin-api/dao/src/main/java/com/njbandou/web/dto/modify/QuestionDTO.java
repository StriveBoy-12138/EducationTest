package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.QuestionEntity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class QuestionDTO {
    private Integer pkId;
    private Integer bankId;
    private Integer questionId;
    private Integer type;
    private String title;
    private String knowledgePoint;
    private String audioName;
    private String audio;
    private String videoName;
    private String video;
    private String fileName;
    private String file;
    private String analysis;
    private String analysisAudioName;
    private String analysisAudio;
    private String analysisVideoName;
    private String analysisVideo;
    private String answer;
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
