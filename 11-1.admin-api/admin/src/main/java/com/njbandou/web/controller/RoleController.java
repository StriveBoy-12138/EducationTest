package com.njbandou.web.controller;

import com.njbandou.web.annotation.CacheLock;
import com.njbandou.web.annotation.CacheParam;
import com.njbandou.web.dto.modify.RoleDTO;
import com.njbandou.web.dto.search.SearchRoleDTO;
import com.njbandou.web.service.RoleService;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/sys/role")
@Api(tags = "用于角色管理的相关接口")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping(value = "/list")
    @RequiresPermissions("role:list")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，获取所有角色信息，分页显示",notes = "统一返回：\n" +
            "pkId-角色主键\n" +
            "name-角色名称\n" +
            "des-角色描述\n" +
            "createTime-创建时间")
    public Result list(@RequestParam(required = false) String keywords,
                       @RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        SearchRoleDTO searchRoleDTO = new SearchRoleDTO();
        searchRoleDTO.setName(keywords);
        searchRoleDTO.setPage(page);
        searchRoleDTO.setPageSize(pageSize);

        return roleService.getPage(searchRoleDTO);
    }

    @GetMapping(value = "/all")
    @RequiresPermissions("role:list")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，获取所有角色信息",notes = "统一返回：\n" +
            "pkId-角色主键\n" +
            "name-角色名称\n" +
            "des-角色描述\n" +
            "createTime-创建时间")
    public Result all() {
        return roleService.getAll();
    }

    @CacheLock(prefix = "roleSave")
    @PostMapping(value = "/save")
    @RequiresPermissions("role:add")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，添加角色信息",notes = "返回“添加成功”")
    public Result add(@CacheParam @RequestBody RoleDTO roleDTO) {
        return roleService.add(roleDTO);
    }

    @CacheLock(prefix = "roleUpdate")
    @PostMapping(value = "/update")
    @RequiresPermissions("role:update")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，修改角色信息",notes = "返回成功")
    public Result update(@CacheParam @RequestBody RoleDTO roleDTO) {
        return roleService.update(roleDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("role:delete")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，删除角色信息",notes = "返回成功")
    public Result delete(@RequestBody Integer[] pkIds) {
        return roleService.delete(pkIds);
    }

    @GetMapping(value = "/info")
    @RequiresPermissions("role:list")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，获取角色权限列表信息",notes = "返回权限列表map，key为menuIdList")
    @ApiImplicitParam(name = "id",value = "角色id")
    public Result info(@RequestParam Integer id) {
        Map map = roleService.info(id);
        return ResultUtil.success(map);
    }

}

