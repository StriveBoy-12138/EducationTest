package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.ClassTypeDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.ClassTypeService;
import com.njbandou.web.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/class/type")
public class ClassTypeController {
    @Autowired
    private ClassTypeService classTypeService;

    @GetMapping(value = "/list")
    @RequiresPermissions("class:type:manage")
    public Result getAllClassType() {
        return classTypeService.getAll();
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("class:type:manage")
    public Result addClassType(@Valid @RequestBody ClassTypeDTO classTypeDTO,
                            BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return classTypeService.add(classTypeDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("class:type:manage")
    public Result updateClassType(@Valid @RequestBody ClassTypeDTO classTypeDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return classTypeService.update(classTypeDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("class:type:manage")
    public Result deleteClassType(@RequestBody ClassTypeDTO classTypeDTO) {
        return classTypeService.delete(classTypeDTO);
    }
}
