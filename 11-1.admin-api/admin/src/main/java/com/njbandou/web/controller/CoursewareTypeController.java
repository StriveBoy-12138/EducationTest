package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.CoursewareTypeDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.CoursewareTypeService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/courseware/type")
public class CoursewareTypeController {
    @Autowired
    private CoursewareTypeService coursewareTypeService;

    @GetMapping(value = "/list")
    @RequiresPermissions("courseware:type:manage")
    @ApiOperation(value = "获取课件分类列表所有数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:"
            +"<br>pkId-int(11)-主键"
            +"<br>name-varchar(32)-课件分类名称")
    public Result getAllCoursewareType() {
        return coursewareTypeService.getAll();
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("courseware:type:manage")
    @ApiOperation(value = "根据课件分类名称name,向课程分类列表添加一条数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    public Result addCoursewareType(@Valid @RequestBody @ApiParam(name = "coursewareTypeDTO", value = "课件分类表dto") CoursewareTypeDTO coursewareTypeDTO,
                            BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return coursewareTypeService.add(coursewareTypeDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("courseware:type:manage")
    @ApiOperation(value = "根据课件分类pkId和无重复的课件分类名称name，更新课程分类列表对应数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数," +
            "data返回简单提示:")
    public Result updateCoursewareType(@Valid @RequestBody @ApiParam(name = "coursewareTypeDTO", value = "课件分类表dto")CoursewareTypeDTO coursewareTypeDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return coursewareTypeService.update(coursewareTypeDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("courseware:type:manage")
    @ApiOperation(value = "根据课件分类pkId，删除课程分类列表对应数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    public Result deleteCoursewareType(@RequestBody @ApiParam(name = "coursewareTypeDTO", value = "课件分类表dto") CoursewareTypeDTO coursewareTypeDTO) {
        return coursewareTypeService.delete(coursewareTypeDTO);
    }
}
