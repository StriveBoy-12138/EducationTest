package com.njbandou.web.dto.search;

import lombok.Data;

@Data
public class SearchRuleDTO {
    private Integer[] type;
    private Integer num;
    private Double score;
}
