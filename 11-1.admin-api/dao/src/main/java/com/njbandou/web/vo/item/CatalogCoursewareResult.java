package com.njbandou.web.vo.item;

import lombok.Data;

@Data
public class CatalogCoursewareResult {
    private Integer pkId;
    private Integer catalogId;
    private Integer coursewareId;
    private String coursewareTitle;
    private Integer studyTime;
    private Integer commentTime;
    private Integer sort;
}
