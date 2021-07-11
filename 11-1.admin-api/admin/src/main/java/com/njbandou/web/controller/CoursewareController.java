package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.CoursewareDTO;
import com.njbandou.web.dto.search.SearchCoursewareDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.CoursewareService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.*;
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
    @ApiOperation(value = "根据课程的typeId和title，获取课件表里对应的分页数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回内容:"
            +"<br>pkId-int(11)-主键"
            +"<br>typeId-int(11)-分类表的pk_id"
            +"<br>typeName-varchar(32)-课程分类名称"
            +"<br>title-int(11)-课件名称"
            +"<br>studyTime-int(11)-学习时长"
            +"<br>introduction-varchar(200)-简介"
            +"<br>courseware_type-int(11)-课件类型(0:图文 1:文档 2:图片 3:音频 4:视频 5:外部链接)"
            +"<br>size-double(10,2)-大小"
            +"<br>content-text-内容"
            +"<br>author_id-int(11)-创建人"
            +"<br>delete_flag-int(11)-删除标识(0:未删除 1:已删除)"
            +"<br>create_time-yyyy-MM-dd HH:mm:ss-创建时间"
            +"<br>update_time-yyyy-MM-dd HH:mm:ss-修改时间"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeId", value = "分类id-即分配表的主键pk_id", defaultValue = "0", required = false),
            @ApiImplicitParam(name = "title", value = "课程名称", defaultValue = "", required = false),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", defaultValue = "10", required = false),
            @ApiImplicitParam(name = "page", value = "分页页码数", defaultValue = "1", required = false)
    })
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
    @ApiOperation(value = "根据课件名称title,向课件表添加一条数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    public Result add(@RequestBody @ApiParam(name = "coursewareDTO", value = "课件表dto")CoursewareDTO coursewareDTO) {
        return coursewareService.add(coursewareDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("courseware:update")
    @ApiOperation(value = "根据课件主键pk_id和课件名称title,更新课件表的一条数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数," +
            "data返回简单提示:")
    public Result update(@Valid @RequestBody @ApiParam(name = "coursewareDTO", value = "课件表dto")CoursewareDTO coursewareDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidateParamException(bindingResult);
        }

        return coursewareService.update(coursewareDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("courseware:delete")
    @ApiOperation(value = "根据课件主键pkId，批量删除课件表对应数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    @ApiImplicitParam(name = "coursewareIds",value = "int数组-课件表主键pk_id")
    public Result delete(@RequestBody Integer[] coursewareIds) {
        return coursewareService.delete(coursewareIds);
    }
}
