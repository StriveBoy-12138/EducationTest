package com.njbandou.web.dto.search;

import lombok.Data;

@Data
public class SearchUserDTO extends BaseSearchDTO {
    private Integer title;
    private Integer isEnabled;
    private Integer[] organizationIds;
    private Integer[] userIds;
    private Integer[] exceptUserIds;
    private String keyword;
    private String userAccount;
    private String userName;
}
