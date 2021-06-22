package com.njbandou.web.vo.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.njbandou.web.entity.ClassEntity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ClassResult {
    private Integer pkId;
    private Integer typeId;
    private String typeName;
    private String title;
    private Integer credit;
    private Integer preventionCheat;
    private Integer videoDrag;
    private Integer creditAcquisition;
    private String studyTime;
    private String picture;
    private String des;
    private Integer classCharge;
    private Integer collectNum;
    private Integer upNum;
    private String inviteUser;
    private String inviteTitle;
    private String inviteOrganization;
    private Integer inviteAll;
    private Integer paper;
    private String paperName;
    private Integer status;
    private Integer authorId;
    private String authorName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp publishTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;

    public static ClassResult setResult(ClassEntity classEntity, String paperName) {
        ClassResult classResult = new ClassResult();
        classResult.setPkId(classEntity.getPkId());
        classResult.setTypeId(classEntity.getTypeId());
        classResult.setTitle(classEntity.getTitle());
        classResult.setCredit(classEntity.getCredit());
        classResult.setPreventionCheat(classEntity.getPreventionCheat());
        classResult.setVideoDrag(classEntity.getVideoDrag());
        classResult.setCreditAcquisition(classEntity.getCreditAcquisition());
        classResult.setStudyTime(classEntity.getStudyTime());
        classResult.setPicture(classEntity.getPicture());
        classResult.setDes(classEntity.getDes());
        classResult.setClassCharge(classEntity.getClassCharge());
        classResult.setCollectNum(classEntity.getCollectNum());
        classResult.setUpNum(classEntity.getUpNum());
        classResult.setInviteUser(classEntity.getInviteUser());
        classResult.setInviteTitle(classEntity.getInviteTitle());
        classResult.setInviteOrganization(classEntity.getInviteOrganization());
        classResult.setInviteAll(classEntity.getInviteAll());
        classResult.setPaper(classEntity.getPaper());
        classResult.setPaperName(paperName);
        classResult.setStatus(classEntity.getStatus());
        classResult.setAuthorId(classEntity.getAuthorId());
        classResult.setCreateTime(classEntity.getCreateTime());
        classResult.setPublishTime(classEntity.getPublishTime());
        classResult.setUpdateTime(classEntity.getUpdateTime());

        return classResult;
    }
}
