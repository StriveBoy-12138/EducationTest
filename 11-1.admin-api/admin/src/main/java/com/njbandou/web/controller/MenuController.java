package com.njbandou.web.controller;

import com.njbandou.web.service.MenuService;
import com.njbandou.web.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sys/menu")
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    @GetMapping(value = "/list")
    @RequiresPermissions("role:manage")
    public Result list() {
        return menuService.getAll();
    }
}
