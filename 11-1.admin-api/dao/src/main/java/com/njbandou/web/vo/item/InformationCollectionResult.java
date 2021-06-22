package com.njbandou.web.vo.item;

import lombok.Data;

@Data
public class InformationCollectionResult {
    private Integer pkId;
    private Integer relationId;
    private Integer type;
    private String title;
    private Integer isRequired;
    private Integer isAble;
    private String option;
    private Integer sort;
}
