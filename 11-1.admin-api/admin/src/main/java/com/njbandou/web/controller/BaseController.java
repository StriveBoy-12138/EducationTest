package com.njbandou.web.controller;

import com.njbandou.web.JWTUtils;
import com.njbandou.web.constant.UserConstant;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class BaseController {

    public Integer getAdminIdFromHeader(HttpServletRequest request) {

        String token = request.getHeader("Authorization");

        Map map = JWTUtils.getClaims(UserConstant.JWT_SECRET, token);

        return Integer.valueOf((String) map.get("pkId"));
    }

}
