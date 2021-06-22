package com.njbandou.web.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_class", schema = "db_examination", catalog = "")
@Where(clause = "delete_flag = 0")
@SQLDelete(sql = "update t_class set delete_flag = 1 where pk_id = ?")
public class ClassEntity {
    private Integer pkId;
    private Integer typeId;
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
    private Integer status;
    private Integer deleteFlag;
    private Integer authorId;
    private Timestamp createTime;
    private Timestamp publishTime;
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
    @Column(name = "type_id")
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
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
    @Column(name = "credit")
    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    @Basic
    @Column(name = "prevention_cheat")
    public Integer getPreventionCheat() {
        return preventionCheat;
    }

    public void setPreventionCheat(Integer preventionCheat) {
        this.preventionCheat = preventionCheat;
    }

    @Basic
    @Column(name = "video_drag")
    public Integer getVideoDrag() {
        return videoDrag;
    }

    public void setVideoDrag(Integer videoDrag) {
        this.videoDrag = videoDrag;
    }

    @Basic
    @Column(name = "credit_acquisition")
    public Integer getCreditAcquisition() {
        return creditAcquisition;
    }

    public void setCreditAcquisition(Integer creditAcquisition) {
        this.creditAcquisition = creditAcquisition;
    }

    @Basic
    @Column(name = "study_time")
    public String getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(String studyTime) {
        this.studyTime = studyTime;
    }

    @Basic
    @Column(name = "picture")
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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
    @Column(name = "class_charge")
    public Integer getClassCharge() {
        return classCharge;
    }

    public void setClassCharge(Integer classCharge) {
        this.classCharge = classCharge;
    }

    @Basic
    @Column(name = "collect_num")
    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    @Basic
    @Column(name = "up_num")
    public Integer getUpNum() {
        return upNum;
    }

    public void setUpNum(Integer upNum) {
        this.upNum = upNum;
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
    @Column(name = "paper")
    public Integer getPaper() {
        return paper;
    }

    public void setPaper(Integer paper) {
        this.paper = paper;
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
    @Column(name = "delete_flag")
    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
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
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "publish_time")
    public Timestamp getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
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
        ClassEntity that = (ClassEntity) o;
        return pkId == that.pkId &&
                typeId == that.typeId &&
                credit == that.credit &&
                preventionCheat == that.preventionCheat &&
                videoDrag == that.videoDrag &&
                creditAcquisition == that.creditAcquisition &&
                classCharge == that.classCharge &&
                collectNum == that.collectNum &&
                upNum == that.upNum &&
                inviteAll == that.inviteAll &&
                paper == that.paper &&
                status == that.status &&
                deleteFlag == that.deleteFlag &&
                authorId == that.authorId &&
                Objects.equals(title, that.title) &&
                Objects.equals(studyTime, that.studyTime) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(des, that.des) &&
                Objects.equals(inviteUser, that.inviteUser) &&
                Objects.equals(inviteTitle, that.inviteTitle) &&
                Objects.equals(inviteOrganization, that.inviteOrganization) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(publishTime, that.publishTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkId, typeId, title, credit, preventionCheat, videoDrag, creditAcquisition, studyTime, picture, des, classCharge, collectNum, upNum, inviteUser, inviteTitle, inviteOrganization, inviteAll, paper, status, deleteFlag, authorId, createTime, publishTime, updateTime);
    }
}
