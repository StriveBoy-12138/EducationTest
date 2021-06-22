package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.CoursewareTypeDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.CoursewareTypeService;
import com.njbandou.web.vo.Result;
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
    public Result getAllCoursewareType() {
        return coursewareTypeService.getAll();
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("courseware:type:manage")
    public Result addCoursewareType(@Valid @RequestBody CoursewareTypeDTO coursewareTypeDTO,
                            BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return coursewareTypeService.add(coursewareTypeDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("courseware:type:manage")
    public Result updateCoursewareType(@Valid @RequestBody CoursewareTypeDTO coursewareTypeDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return coursewareTypeService.update(coursewareTypeDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("courseware:type:manage")
    public Result deleteCoursewareType(@RequestBody CoursewareTypeDTO coursewareTypeDTO) {
        return coursewareTypeService.delete(coursewareTypeDTO);
    }
}
