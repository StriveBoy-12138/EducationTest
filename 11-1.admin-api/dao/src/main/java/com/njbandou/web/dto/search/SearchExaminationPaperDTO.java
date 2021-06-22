package com.njbandou.web.dto.search;

import lombok.Data;

@Data
public class SearchExaminationPaperDTO extends BaseSearchDTO {
    private Integer paperId;
    private Integer status;
    private Integer isJudge;
    private Integer isMark;
    private String startTime;
    private String endTime;
    private String userInfo;
    private String keyWord;
    private String finishTime;
    private String scores;
}
