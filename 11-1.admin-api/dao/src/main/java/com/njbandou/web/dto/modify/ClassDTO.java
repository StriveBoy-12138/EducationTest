package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.ClassEntity;
import io.swagger.annotations.*;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@ApiModel(value = "课程表单条数据dto",description = "从客户端，由用户传来的数据封装在此dto中")
@Data
public class ClassDTO {
    @ApiModelProperty(name ="pk_id",value = "int-课程表主键")
    private Integer pkId;
    @ApiModelProperty(name ="typeId",value = "int-分类id-课程分类表主键pk_id")
    private Integer typeId;

    @NotBlank(message = "课程名称不能为空")
    @ApiModelProperty(name ="title",value = "课程名称-string")
    private String title;
    @ApiModelProperty(name ="credit",value = "学分-int")
    private Integer credit;
    @ApiModelProperty(name ="preventionCheat",value = "防作弊(0:开启 1:关闭)")
    private Integer preventionCheat;
    @ApiModelProperty(name ="videoDrag",value = "拖动视频(0:开启 1:关闭)")
    private Integer videoDrag;
    @ApiModelProperty(name ="creditAcquisition",value = "学分获取(0:完成时获取学分 1:不启用学分)")
    private Integer creditAcquisition;

    @ApiModelProperty(name ="studyTime",value = "开课时间-JSON格式String")
    private String studyTime;
    @ApiModelProperty(name ="picture",value = "封面图片-url")
    private String picture;
    @NotBlank(message = "课程描述不能为空")
    @ApiModelProperty(name ="des",value = "课程描述")
    private String des;
    @ApiModelProperty(name ="classCharge",value = "课程收费(0:开启 1:关闭)")
    private Integer classCharge;
    @ApiModelProperty(name ="classCharge",value = "状态(0:未发布 1:已发布 2:已结课)")
    private Integer status;
    @ApiModelProperty(name ="inviteUser",value = "用户id(用,分隔开)-string-用户表主键")
    private String inviteUser;
    @ApiModelProperty(name ="inviteTitle",value = "标签id(用,分隔开)-string-标签表t_title主键")
    private String inviteTitle;
    @ApiModelProperty(name ="inviteOrganization",value = "组织id(用,分隔开)-string-组织表主键")
    private String inviteOrganization;
    @ApiModelProperty(name ="inviteAll",value = "邀请全部(0:是 1:否)")
    private Integer inviteAll;

    @ApiModelProperty(name ="paper",value = "int-试卷表t_examination_paper的pk_id")
    private Integer paper;
    @ApiModelProperty(name ="paper",value = "int-创建人id-即用户表t_user的pk_id")
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
