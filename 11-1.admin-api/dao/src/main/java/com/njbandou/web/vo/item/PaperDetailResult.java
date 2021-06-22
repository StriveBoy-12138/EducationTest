package com.njbandou.web.vo.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.njbandou.web.entity.PaperEntity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class PaperDetailResult {
    private Integer pkId;
    private String title;
    private String des;
    private Integer typeId;
    private Integer isOpen;
    private Integer isFixed;
    private String questions;
    private String questionRule;
    private Integer status;
    private Integer placeFile;
    private Integer authorId;
    private String inviteUser;
    private String inviteTitle;
    private String inviteOrganization;
    private Integer inviteAll;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;

    public PaperDetailResult(PaperEntity paper) {
        this.pkId = paper.getPkId();
        this.title = paper.getTitle();
        this.des = paper.getDes();
        this.typeId = paper.getTypeId();
        this.isOpen = paper.getIsOpen();
        this.isFixed = paper.getIsFixed();
        this.questions = paper.getQuestions();
        this.questionRule = paper.getQuestionRule();
        this.status = paper.getStatus();
        this.placeFile = paper.getPlaceFile();
        this.authorId = paper.getAuthorId();
        this.inviteUser = paper.getInviteUser();
        this.inviteTitle = paper.getInviteTitle();
        this.inviteOrganization = paper.getInviteOrganization();
        this.inviteAll = paper.getInviteAll();
        this.createTime = paper.getCreateTime();
        this.updateTime = paper.getUpdateTime();
    }
}
