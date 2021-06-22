package com.njbandou.web.vo.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class QuestionResult {
    private Integer pkId;
    private Integer bankId;
    private Integer questionId;
    private Integer type;
    private String typeName;
    private String title;
    private String knowledgePoint;
    private List<QuestionKnowledgePointResult> questionKnowledgePoint;
    private String audioName;
    private String audio;
    private String videoName;
    private String video;
    private String fileName;
    private String[] fileNameList = new String[0];
    private String file;
    private String[] fileList = new String[0];
    private String analysis;
    private String analysisAudioName;
    private String analysisAudio;
    private String analysisVideoName;
    private String analysisVideo;
    private String answer;
    private Integer difficulty;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;
    private Double score = 0.0;
}
