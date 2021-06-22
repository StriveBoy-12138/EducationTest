package com.njbandou.web.vo.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CoursewareResult {
    private Integer pkId;
    private String title;
    private Integer typeId;
    private String typeName;
    private Integer studyTime;
    private String introduction;
    private Integer coursewareType;
    private Double size;
    private String content;
    private Integer authorId;
    private String authorName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;
}
