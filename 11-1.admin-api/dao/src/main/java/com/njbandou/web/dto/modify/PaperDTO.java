package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.PaperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
@ApiModel(value = "试卷对象DTO", description = "从客户端，由用户传入的数据封装在此DTO中")
@Data
public class PaperDTO {
    @ApiModelProperty(value = "题目主键", name = "pkId")
    private Integer pkId;
    @NotBlank(message = "试卷名称不能为空")
    @ApiModelProperty(value = "名称", name = "title")
    private String title;
    @ApiModelProperty(value = "描述", name = "des")
    private String des;
    @ApiModelProperty(value = "试卷分类id", name = "typeId")
    private Integer typeId;
    @ApiModelProperty(value = "是否公开(0:是 1:否)", name = "isOpen")
    private Integer isOpen;
    @ApiModelProperty(value = "是否固定(0:是 1:否)", name = "isFixed")
    private Integer isFixed;
    @ApiModelProperty(value = "创建人", name = "authorId")
    private Integer authorId;
    @ApiModelProperty(value = "题目(JSONString)", name = "questions")
    private String questions;
    @ApiModelProperty(value = "选题规则(题目随机时)(JSONString)", name = "questionRule")
    private String questionRule;
    @ApiModelProperty(value = "状态(0:待发布 1:已发布)", name = "status")
    private Integer status;
    @ApiModelProperty(value = "归档(0:未归档 1:已归档)", name = "placeFile")
    private Integer placeFile;
    @ApiModelProperty(value = "用户id(,隔开)", name = "inviteUser")
    private String inviteUser;
    @ApiModelProperty(value = "标签id(,隔开)", name = "inviteTitle")
    private String inviteTitle;
    @ApiModelProperty(value = "组织id(,隔开)", name = "inviteOrganization")
    private String inviteOrganization;
    @ApiModelProperty(value = "邀请全部(0:是 1:否)", name = "inviteAll")
    private Integer inviteAll;


    private Boolean isSet = true;

    private PaperEntity set(PaperEntity paper){
        paper.setTypeId(typeId);
        paper.setTitle(title);
        paper.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return paper;
    }

    public PaperEntity insert(){
        PaperEntity paper = set(new PaperEntity());
        paper.setIsOpen(isOpen);
        paper.setIsFixed(isFixed);
        paper.setAuthorId(authorId);
        paper.setInviteUser("");
        paper.setInviteTitle("");
        paper.setInviteOrganization("");
        paper.setInviteAll(1);
        paper.setDes("");
        paper.setQuestions("[]");
        paper.setQuestionRule("{}");
        paper.setPlaceFile(0);
        paper.setStatus(0);
        paper.setDeleteFlag(0);
        paper.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return paper;
    }

    public PaperEntity update(PaperEntity paper){
        paper.setDes(des);

        return set(paper);
    }

    public PaperEntity setUser(PaperEntity paper) {
        paper.setInviteUser((StringUtils.isNotBlank(paper.getInviteUser()) && isSet) ? paper.getInviteUser()+","+inviteUser:inviteUser);
        paper.setInviteTitle((StringUtils.isNotBlank(paper.getInviteTitle()) && isSet) ? paper.getInviteTitle()+","+inviteTitle:inviteTitle);
        paper.setInviteOrganization((StringUtils.isNotBlank(paper.getInviteOrganization()) && isSet) ? paper.getInviteOrganization()+","+inviteOrganization:inviteOrganization);
        paper.setInviteAll(inviteAll);
        paper.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return paper;
    }

    public PaperEntity setPaperQuestions(PaperEntity paper) {
        paper.setQuestions(questions);
        paper.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return paper;
    }

    public PaperEntity setRule(PaperEntity paper) {
        paper.setQuestionRule(questionRule);
        paper.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return paper;
    }

    public PaperEntity placeFilePaper(PaperEntity paper) {
        paper.setPlaceFile(placeFile);
        paper.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return paper;
    }
}
