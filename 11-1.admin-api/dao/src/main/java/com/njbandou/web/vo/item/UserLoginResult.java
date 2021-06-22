package com.njbandou.web.vo.item;

import lombok.Data;

import java.util.List;

@Data
public class UserLoginResult {
    private Integer pkId;
    private String account;
    private String nickname;
    private String avatar;
    private List<String> access;
}
