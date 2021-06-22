package com.njbandou.web.vo.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ExaminationPaperResult {
    private Integer pkId;
    private Integer userId;
    private String userAccount;
    private String userName;
    private String organizationName;
    private String userInfo;
    private Integer paperId;
    private String questions;
    private Double userScore;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp finishTime;
    private Integer useTime;
    private Integer isJudge;
    private Integer isMark;
    private Integer status;
}
