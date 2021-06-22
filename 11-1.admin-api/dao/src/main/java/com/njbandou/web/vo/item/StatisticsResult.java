package com.njbandou.web.vo.item;

import lombok.Data;

@Data
public class StatisticsResult {
    private Integer paperNum;
    private Integer questionNum;
    private Integer userNum;
    private Integer classNum;
    private Integer coursewareNum;

    public StatisticsResult(Integer paperNum, Integer questionNum, Integer userNum, Integer classNum, Integer coursewareNum) {
        this.paperNum = paperNum;
        this.questionNum = questionNum;
        this.userNum = userNum;
        this.classNum = classNum;
        this.coursewareNum = coursewareNum;
    }
}
