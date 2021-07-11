package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.TitleDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.TitleService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/title")
@Api(tags = "用于标签管理的相关接口")
public class TitleController {
    @Autowired
    private TitleService titleService;

    @GetMapping(value = "/list")
    @RequiresPermissions("title:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，获取所有标签",notes = "统一返回：\n" +
            "pkId-标签主键\n" +
            "name-标签名称\n" +
            "sort-排序号\n")
    public Result getAllTitle(@RequestParam(required = false, name = "exceptTitleIds[]") Integer[] exceptTitleIds) {
        return titleService.getAll(exceptTitleIds);
    }

    @GetMapping(value = "/invite/list")
    @RequiresPermissions("title:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，通过id搜索标签",notes = "统一返回：\n" +
            "pkId-标签主键\n" +
            "name-标签名称\n" +
            "sort-排序号\n")
    @ApiImplicitParam(name = "titleIds",value = "标签数组")
    public Result getInviteTitle(@RequestParam(required = false, name = "titleIds[]") Integer[] titleIds) {
        return titleService.getInviteTitle(titleIds);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("title:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，增加标签",notes = "返回“操作成功”")
    public Result addTitle(@Valid @RequestBody TitleDTO titleDTO,
                            BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return titleService.add(titleDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("title:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，修改标签",notes = "返回“操作成功”")
    public Result updateTitle(@Valid @RequestBody TitleDTO titleDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return titleService.update(titleDTO);
    }

    @PostMapping(value = "/move")
    @RequiresPermissions("title:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，移动标签",notes = "返回“操作成功”")
    public Result moveProject(@RequestBody TitleDTO titleDTO) {
        return titleService.move(titleDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("title:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，删除标签",notes = "返回“操作成功”")
    public Result deleteTitle(@RequestBody TitleDTO titleDTO) {
        return titleService.delete(titleDTO);
    }
}
