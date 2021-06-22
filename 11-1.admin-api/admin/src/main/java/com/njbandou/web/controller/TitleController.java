package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.TitleDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.TitleService;
import com.njbandou.web.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/title")
public class TitleController {
    @Autowired
    private TitleService titleService;

    @GetMapping(value = "/list")
    @RequiresPermissions("title:manage")
    public Result getAllTitle(@RequestParam(required = false, name = "exceptTitleIds[]") Integer[] exceptTitleIds) {
        return titleService.getAll(exceptTitleIds);
    }

    @GetMapping(value = "/invite/list")
    @RequiresPermissions("title:manage")
    public Result getInviteTitle(@RequestParam(required = false, name = "titleIds[]") Integer[] titleIds) {
        return titleService.getInviteTitle(titleIds);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("title:manage")
    public Result addTitle(@Valid @RequestBody TitleDTO titleDTO,
                            BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return titleService.add(titleDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("title:manage")
    public Result updateTitle(@Valid @RequestBody TitleDTO titleDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return titleService.update(titleDTO);
    }

    @PostMapping(value = "/move")
    @RequiresPermissions("title:manage")
    public Result moveProject(@RequestBody TitleDTO titleDTO) {
        return titleService.move(titleDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("title:manage")
    public Result deleteTitle(@RequestBody TitleDTO titleDTO) {
        return titleService.delete(titleDTO);
    }
}
