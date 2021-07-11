package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.OrganizationDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.OrganizationService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/organization")
@Api(tags = "用于组织架构的相关接口")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    @GetMapping(value = "/all")
    @RequiresPermissions("organization:list")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，获取所有组织架构信息",notes = "返回组织架构的树：{pkId，parentId，expand，title，children{...}}")
    public Result getAllOrganization(@RequestParam(required = false, name = "exceptOrganizationIds[]") Integer[] exceptOrganizationIds) {
        return organizationService.getAll(exceptOrganizationIds);
    }

    @GetMapping(value = "/invite/list")
    @RequiresPermissions("organization:list")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，通过id获得组织",notes = "统一返回：\n" +
            "pkId-组织id\n" +
            "title-组织名称" )
    public Result getInviteOrganization(@RequestParam(required = false, name = "organizationIds[]") Integer[] organizationIds) {
        return organizationService.getInviteOrganization(organizationIds);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("organization:add")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，添加组织",notes = "返回“操作成功”" )
    public Result addOrganization(@Valid @RequestBody OrganizationDTO damageReasonDTO,
                              BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return organizationService.add(damageReasonDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("organization:update")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，修改组织",notes = "返回“操作成功”" )
    public Result updateOrganization(@Valid @RequestBody OrganizationDTO damageReasonDTO,
                                 BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return organizationService.update(damageReasonDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("organization:delete")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，删除组织",notes = "返回“操作成功”" )
    public Result deleteOrganization(@RequestBody OrganizationDTO damageReasonDTO) {
        return organizationService.delete(damageReasonDTO);
    }
}
