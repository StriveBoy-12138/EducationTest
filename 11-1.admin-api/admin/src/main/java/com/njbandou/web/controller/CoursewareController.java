package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.CoursewareDTO;
import com.njbandou.web.dto.search.SearchCoursewareDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.CoursewareService;
import com.njbandou.web.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/courseware")
public class CoursewareController {
    @Autowired
    private CoursewareService coursewareService;

    @GetMapping(value = "/list")
    @RequiresPermissions("courseware:list")
    public Result list(@RequestParam(required = false, defaultValue = "0") Integer typeId,
                       @RequestParam(required = false, defaultValue = "") String title,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false, defaultValue = "1") Integer page) {
        SearchCoursewareDTO searchCoursewareDTO = new SearchCoursewareDTO();
        searchCoursewareDTO.setTypeId(typeId);
        searchCoursewareDTO.setTitle(title);
        searchCoursewareDTO.setPage(page);
        searchCoursewareDTO.setPageSize(pageSize);
        return coursewareService.getPage(searchCoursewareDTO);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("courseware:add")
    public Result add(@RequestBody CoursewareDTO coursewareDTO) {
        return coursewareService.add(coursewareDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("courseware:update")
    public Result update(@Valid @RequestBody CoursewareDTO coursewareDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidateParamException(bindingResult);
        }

        return coursewareService.update(coursewareDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("courseware:delete")
    public Result delete(@RequestBody Integer[] coursewareIds) {
        return coursewareService.delete(coursewareIds);
    }
}
