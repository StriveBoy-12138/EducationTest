package com.njbandou.web.controller;

import com.njbandou.web.annotation.CacheLock;
import com.njbandou.web.annotation.CacheParam;
import com.njbandou.web.dto.modify.RoleDTO;
import com.njbandou.web.dto.search.SearchRoleDTO;
import com.njbandou.web.service.RoleService;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/sys/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping(value = "/list")
    @RequiresPermissions("role:list")
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
    public Result all() {
        return roleService.getAll();
    }

    @CacheLock(prefix = "roleSave")
    @PostMapping(value = "/save")
    @RequiresPermissions("role:add")
    public Result add(@CacheParam @RequestBody RoleDTO roleDTO) {
        return roleService.add(roleDTO);
    }

    @CacheLock(prefix = "roleUpdate")
    @PostMapping(value = "/update")
    @RequiresPermissions("role:update")
    public Result update(@CacheParam @RequestBody RoleDTO roleDTO) {
        return roleService.update(roleDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("role:delete")
    public Result delete(@RequestBody Integer[] pkIds) {
        return roleService.delete(pkIds);
    }

    @GetMapping(value = "/info")
    @RequiresPermissions("role:list")
    public Result info(@RequestParam Integer id) {
        Map map = roleService.info(id);
        return ResultUtil.success(map);
    }

}

