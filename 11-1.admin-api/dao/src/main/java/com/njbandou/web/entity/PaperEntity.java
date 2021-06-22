package com.njbandou.web.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_paper", schema = "db_examination", catalog = "")
@Where(clause = "delete_flag = 0")
@SQLDelete(sql = "update t_paper set delete_flag = 1 where pk_id = ?")
public class PaperEntity {
    private Integer pkId;
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
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "des")
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Basic
    @Column(name = "type_id")
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    @Basic
    @Column(name = "is_open")
    public Integer getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }

    @Basic
    @Column(name = "is_fixed")
    public Integer getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(Integer isFixed) {
        this.isFixed = isFixed;
    }

    @Basic
    @Column(name = "author_id")
    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    @Basic
    @Column(name = "questions")
    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    @Basic
    @Column(name = "question_rule")
    public String getQuestionRule() {
        return questionRule;
    }

    public void setQuestionRule(String questionRule) {
        this.questionRule = questionRule;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "place_file")
    public Integer getPlaceFile() {
        return placeFile;
    }

    public void setPlaceFile(Integer placeFile) {
        this.placeFile = placeFile;
    }

    @Basic
    @Column(name = "invite_user")
    public String getInviteUser() {
        return inviteUser;
    }

    public void setInviteUser(String inviteUser) {
        this.inviteUser = inviteUser;
    }

    @Basic
    @Column(name = "invite_title")
    public String getInviteTitle() {
        return inviteTitle;
    }

    public void setInviteTitle(String inviteTitle) {
        this.inviteTitle = inviteTitle;
    }

    @Basic
    @Column(name = "invite_organization")
    public String getInviteOrganization() {
        return inviteOrganization;
    }

    public void setInviteOrganization(String inviteOrganization) {
        this.inviteOrganization = inviteOrganization;
    }

    @Basic
    @Column(name = "invite_all")
    public Integer getInviteAll() {
        return inviteAll;
    }

    public void setInviteAll(Integer inviteAll) {
        this.inviteAll = inviteAll;
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
        PaperEntity that = (PaperEntity) o;
        return pkId == that.pkId &&
                typeId == that.typeId &&
                isOpen == that.isOpen &&
                isFixed == that.isFixed &&
                authorId == that.authorId &&
                status == that.status &&
                placeFile == that.placeFile &&
                inviteAll == that.inviteAll &&
                deleteFlag == that.deleteFlag &&
                Objects.equals(title, that.title) &&
                Objects.equals(des, that.des) &&
                Objects.equals(questions, that.questions) &&
                Objects.equals(questionRule, that.questionRule) &&
                Objects.equals(inviteUser, that.inviteUser) &&
                Objects.equals(inviteTitle, that.inviteTitle) &&
                Objects.equals(inviteOrganization, that.inviteOrganization) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkId, title, des, typeId, isOpen, isFixed, authorId, questions, questionRule, status, placeFile, inviteUser, inviteTitle, inviteOrganization, inviteAll, deleteFlag, createTime, updateTime);
    }
}
