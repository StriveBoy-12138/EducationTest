package com.njbandou.web.vo.item;

import lombok.Data;

@Data
public class QuestionNumberResult {
    private Integer[] type;
    private Integer[] difficulty;

    public QuestionNumberResult(Integer[] type, Integer[] difficulty) {
        this.type = type;
        this.difficulty = difficulty;
    }
}
