package com.njbandou.web.dto.search;

import lombok.Data;

@Data
public class SearchKnowledgePointDTO extends BaseSearchDTO {
    private Integer typeId;
    private String name;
    private Integer[] exceptKnowledgePointIds;
}