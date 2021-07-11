package com.njbandou.web.controller;

import com.njbandou.web.service.MenuService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sys/menu")
@Api(tags = "用于权限管理的相关接口")
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    @GetMapping(value = "/list")
    @RequiresPermissions("role:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，获取全部权限信息",notes = "统一返回：\n" +
            "pkId-权限主键" +
            "parentId-父级id" +
            "title-权限名称" +
            "deleteFlag-删除标志"
    )
    public Result list() {
        return menuService.getAll();
    }
}
