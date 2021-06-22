package com.njbandou.web.vo.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class PaperResult {
    private Integer pkId;
    private String title;
    private Integer testedTime;
    private Integer inExam;
    private String des;
    private Integer typeId;
    private String typeName;
    private Integer isOpen;
    private Integer isFixed;
    private String questions;
    private String questionRule;
    private Integer status;
    private Integer placeFile;
    private Integer authorId;
    private String authorName;
    private String inviteUser;
    private String inviteTitle;
    private String inviteOrganization;
    private Integer inviteAll;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;
}
