package com.njbandou.web.dto.search;

import lombok.Data;

@Data
public class SearchPaperDTO extends BaseSearchDTO {
    private Integer placeFile;
    private Integer typeId;
    private Integer status;
    private Integer isOpen;
    private Integer isFixed;
    private String title;
}
