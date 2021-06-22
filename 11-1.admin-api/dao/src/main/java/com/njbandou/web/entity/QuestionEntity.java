package com.njbandou.web.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_question", schema = "db_examination", catalog = "")
@Where(clause = "delete_flag = 0")
@SQLDelete(sql = "update t_question set delete_flag = 1 where pk_id = ?")
public class QuestionEntity {
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
    @Column(name = "bank_id")
    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
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
    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "knowledge_point")
    public String getKnowledgePoint() {
        return knowledgePoint;
    }

    public void setKnowledgePoint(String knowledgePoint) {
        this.knowledgePoint = knowledgePoint;
    }

    @Basic
    @Column(name = "audio_name")
    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    @Basic
    @Column(name = "audio")
    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    @Basic
    @Column(name = "video_name")
    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    @Basic
    @Column(name = "video")
    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Basic
    @Column(name = "file_name")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Basic
    @Column(name = "file")
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Basic
    @Column(name = "analysis")
    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    @Basic
    @Column(name = "analysis_audio_name")
    public String getAnalysisAudioName() {
        return analysisAudioName;
    }

    public void setAnalysisAudioName(String analysisAudioName) {
        this.analysisAudioName = analysisAudioName;
    }

    @Basic
    @Column(name = "analysis_audio")
    public String getAnalysisAudio() {
        return analysisAudio;
    }

    public void setAnalysisAudio(String analysisAudio) {
        this.analysisAudio = analysisAudio;
    }

    @Basic
    @Column(name = "analysis_video_name")
    public String getAnalysisVideoName() {
        return analysisVideoName;
    }

    public void setAnalysisVideoName(String analysisVideoName) {
        this.analysisVideoName = analysisVideoName;
    }

    @Basic
    @Column(name = "analysis_video")
    public String getAnalysisVideo() {
        return analysisVideo;
    }

    public void setAnalysisVideo(String analysisVideo) {
        this.analysisVideo = analysisVideo;
    }

    @Basic
    @Column(name = "answer")
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Basic
    @Column(name = "difficulty")
    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
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
        QuestionEntity that = (QuestionEntity) o;
        return pkId == that.pkId &&
                bankId == that.bankId &&
                questionId == that.questionId &&
                type == that.type &&
                difficulty == that.difficulty &&
                deleteFlag == that.deleteFlag &&
                Objects.equals(title, that.title) &&
                Objects.equals(knowledgePoint, that.knowledgePoint) &&
                Objects.equals(audioName, that.audioName) &&
                Objects.equals(audio, that.audio) &&
                Objects.equals(videoName, that.videoName) &&
                Objects.equals(video, that.video) &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(file, that.file) &&
                Objects.equals(analysis, that.analysis) &&
                Objects.equals(analysisAudioName, that.analysisAudioName) &&
                Objects.equals(analysisAudio, that.analysisAudio) &&
                Objects.equals(analysisVideoName, that.analysisVideoName) &&
                Objects.equals(analysisVideo, that.analysisVideo) &&
                Objects.equals(answer, that.answer) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkId, bankId, questionId, type, title, knowledgePoint, audioName, audio, videoName, video, fileName, file, analysis, analysisAudioName, analysisAudio, analysisVideoName, analysisVideo, answer, difficulty, deleteFlag, createTime, updateTime);
    }
}
