package com.njbandou.web.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_catalog_courseware", schema = "db_examination", catalog = "")
@Where(clause = "delete_flag = 0")
@SQLDelete(sql = "update t_catalog_courseware set delete_flag = 1 where pk_id = ?")
public class CatalogCoursewareEntity {
    private Integer pkId;
    private Integer catalogId;
    private Integer coursewareId;
    private Integer commentTime;
    private Integer sort;
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
    @Column(name = "catalog_id")
    public Integer getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Integer catalogId) {
        this.catalogId = catalogId;
    }

    @Basic
    @Column(name = "courseware_id")
    public Integer getCoursewareId() {
        return coursewareId;
    }

    public void setCoursewareId(Integer coursewareId) {
        this.coursewareId = coursewareId;
    }

    @Basic
    @Column(name = "comment_time")
    public Integer getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Integer commentTime) {
        this.commentTime = commentTime;
    }

    @Basic
    @Column(name = "sort")
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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
        CatalogCoursewareEntity that = (CatalogCoursewareEntity) o;
        return pkId == that.pkId &&
                catalogId == that.catalogId &&
                coursewareId == that.coursewareId &&
                commentTime == that.commentTime &&
                sort == that.sort &&
                deleteFlag == that.deleteFlag &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkId, catalogId, coursewareId, commentTime, sort, deleteFlag, createTime, updateTime);
    }
}
