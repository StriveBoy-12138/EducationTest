package com.njbandou.web.dto.search;

import lombok.Data;

@Data
public class SearchQuestionBankDTO extends BaseSearchDTO {
    private Integer typeId;
    private String name;
}
