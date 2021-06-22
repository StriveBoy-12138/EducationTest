package com.njbandou.web.dto.search;

import lombok.Data;

@Data
public class SearchCoursewareDTO extends BaseSearchDTO {
    private Integer typeId;
    private String title;
}
