package com.njbandou.web.dto.search;

import lombok.Data;

import java.util.List;

@Data
public class SearchQuestionDTO extends BaseSearchDTO {
    private Integer bankId;
    private Integer questionId;
    private Integer type;
    private String title;
    private Integer difficulty;
    private Integer ruleId;
    private List<SearchRuleDTO> rules;
}
