package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.ClassEntity;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
public class ClassDTO {
    private Integer pkId;
    private Integer typeId;

    @NotBlank(message = "课程名称不能为空")
    private String title;
    private Integer credit;
    private Integer preventionCheat;
    private Integer videoDrag;
    private Integer creditAcquisition;
    private String studyTime;
    private String picture;
    @NotBlank(message = "课程描述不能为空")
    private String des;
    private Integer classCharge;
    private Integer status;
    private String inviteUser;
    private String inviteTitle;
    private String inviteOrganization;
    private Integer inviteAll;
    private Integer paper;
    private Integer authorId;

    private Boolean isSet = true;

    private ClassEntity set(ClassEntity classEntity){
        classEntity.setTypeId(typeId);
        classEntity.setTitle(title);
        classEntity.setCredit(credit);
        classEntity.setPreventionCheat(preventionCheat);
        classEntity.setVideoDrag(videoDrag);
        classEntity.setCreditAcquisition(creditAcquisition);
        classEntity.setStudyTime(studyTime);
        classEntity.setPicture(picture);
        classEntity.setDes(des);
        classEntity.setClassCharge(classCharge);
        classEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return classEntity;
    }

    public ClassEntity insert(){
        ClassEntity classEntity = set(new ClassEntity());
        classEntity.setAuthorId(authorId);
        classEntity.setInviteUser("");
        classEntity.setInviteTitle("");
        classEntity.setInviteOrganization("");
        classEntity.setInviteAll(1);
        classEntity.setPaper(0);
        classEntity.setCollectNum(0);
        classEntity.setUpNum(0);
        classEntity.setStatus(0);
        classEntity.setDeleteFlag(0);
        classEntity.setPublishTime(Timestamp.valueOf("1999-01-01 00:00:00"));
        classEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return classEntity;
    }

    public ClassEntity update(ClassEntity classEntity){
        return set(classEntity);
    }

    public ClassEntity setClassPaper(ClassEntity classEntity){
        classEntity.setPaper(paper);
        classEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return classEntity;
    }

    public ClassEntity setUser(ClassEntity classEntity){
        classEntity.setInviteUser((StringUtils.isNotBlank(classEntity.getInviteUser()) && isSet) ? classEntity.getInviteUser()+","+inviteUser:inviteUser);
        classEntity.setInviteTitle((StringUtils.isNotBlank(classEntity.getInviteTitle()) && isSet) ? classEntity.getInviteTitle()+","+inviteTitle:inviteTitle);
        classEntity.setInviteOrganization((StringUtils.isNotBlank(classEntity.getInviteOrganization()) && isSet) ? classEntity.getInviteOrganization()+","+inviteOrganization:inviteOrganization);
        classEntity.setInviteAll(inviteAll);
        classEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return classEntity;
    }
}
