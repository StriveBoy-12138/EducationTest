package com.njbandou.web.vo.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class QuestionKnowledgePointResult {
    private Integer pkId;
    private String name;

    public QuestionKnowledgePointResult(Integer pkId, String name) {
        this.pkId = pkId;
        this.name = name;
    }
}
