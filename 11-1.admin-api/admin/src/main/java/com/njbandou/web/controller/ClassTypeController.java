package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.ClassTypeDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.ClassTypeService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.*;
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
    @ApiOperation(value = "获取课程分类列表所有数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回内容:"
            +"<br>pkId-int(11)-主键"
            +"<br>name-varchar(32)-课程分类名称")
    public Result getAllClassType() {
        return classTypeService.getAll();
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "根据课程分类名称name,向课程分类列表添加一条数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    @RequiresPermissions("class:type:manage")
    public Result addClassType(@Valid @RequestBody @ApiParam(name = "classTypeDTO", value = "课程分类表dto")  ClassTypeDTO classTypeDTO,
                            BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return classTypeService.add(classTypeDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("class:type:manage")
    @ApiOperation(value = "根据课程分类pkId和无重复的课程分类名称name，更新课程分类列表对应数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数," +
            "data返回简单提示:")
    public Result updateClassType(@Valid @RequestBody @ApiParam(name = "classTypeDTO", value = "课程分类表dto")  ClassTypeDTO classTypeDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return classTypeService.update(classTypeDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("class:type:manage")
    @ApiOperation(value = "根据课程分类pkId，删除课程分类列表对应数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    public Result deleteClassType(@RequestBody @ApiParam(name = "classTypeDTO", value = "课程分类表dto")  ClassTypeDTO classTypeDTO) {
        return classTypeService.delete(classTypeDTO);
    }
}
