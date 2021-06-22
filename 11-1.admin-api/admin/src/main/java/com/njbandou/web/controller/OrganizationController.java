package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.OrganizationDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.OrganizationService;
import com.njbandou.web.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/organization")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    @GetMapping(value = "/all")
    @RequiresPermissions("organization:list")
    public Result getAllOrganization(@RequestParam(required = false, name = "exceptOrganizationIds[]") Integer[] exceptOrganizationIds) {
        return organizationService.getAll(exceptOrganizationIds);
    }

    @GetMapping(value = "/invite/list")
    @RequiresPermissions("organization:list")
    public Result getInviteOrganization(@RequestParam(required = false, name = "organizationIds[]") Integer[] organizationIds) {
        return organizationService.getInviteOrganization(organizationIds);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("organization:add")
    public Result addOrganization(@Valid @RequestBody OrganizationDTO damageReasonDTO,
                              BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return organizationService.add(damageReasonDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("organization:update")
    public Result updateOrganization(@Valid @RequestBody OrganizationDTO damageReasonDTO,
                                 BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return organizationService.update(damageReasonDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("organization:delete")
    public Result deleteOrganization(@RequestBody OrganizationDTO damageReasonDTO) {
        return organizationService.delete(damageReasonDTO);
    }
}
