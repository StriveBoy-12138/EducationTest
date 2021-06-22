package com.njbandou.web.vo.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserResult {
    private Integer pkId;
    private Integer title;
    private String titleName;
    private Integer type;
    private String account;
    private String nickname;
    private String avatar;
    private String phone;
    private Integer organizationId;
    private String organizationName;
    private String otherInfo;
    private Integer roleId;
    private String roleName;
    private Integer isEnabled;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;
}
