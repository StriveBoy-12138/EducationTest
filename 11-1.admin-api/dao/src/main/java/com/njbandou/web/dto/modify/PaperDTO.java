package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.PaperEntity;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
public class PaperDTO {
    private Integer pkId;
    @NotBlank(message = "试卷名称不能为空")
    private String title;
    private String des;
    private Integer typeId;
    private Integer isOpen;
    private Integer isFixed;
    private Integer authorId;
    private String questions;
    private String questionRule;
    private Integer status;
    private Integer placeFile;
    private String inviteUser;
    private String inviteTitle;
    private String inviteOrganization;
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
