package com.njbandou.web.controller;

import com.fasterxml.jackson.annotation.*;
import com.njbandou.web.dto.modify.*;
import com.njbandou.web.dto.search.*;
import com.njbandou.web.exception.*;
import com.njbandou.web.service.*;
import com.njbandou.web.vo.*;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.sql.*;

@RestController
@RequestMapping("/class")
public class ClassController {
    @Autowired
    private ClassService classService;

    @GetMapping(value = "/list")
    @RequiresPermissions("class:list")
    @ApiOperation(value = "根据课程的typeId和title，获取课程表里对应的分页数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回内容:"
            +"<br>pkId-int(11)-主键"
            +"<br>typeId-int(11)-分类表的pk_id"
            +"<br>typeName-varchar(32)-分类名称"
            +"<br>credit-int(11)-学分"
            +"<br>preventionCheat-int(11)-防作弊(0:开启 1:关闭)"
            +"<br>videoDrag-int(11)-拖动视频(0:开启 1:关闭)"
            +"<br>creditAcquisition-int(11)-学分获取(0:完成时获取学分 1:不启用学分)"
            +"<br>studyTime-varchar(64)-开课时间(JSONString)"
            +"<br>picture-varchar(200)-封面图片"
            +"<br>des-text-描述"
            +"<br>classCharge-int(11)-课程收费(0:开启 1:关闭)"
            +"<br>collectNum-int(11)-收藏次数"
            +"<br>upNum-int(11)-点赞次数"
            +"<br>inviteUser-varchar(500)-用户id(,隔开)"
            +"<br>inviteTitle-varchar(500)-标签id(,隔开)"
            +"<br>inviteOrganization-varchar(500)-组织id(,隔开)"
            +"<br>inviteAll-int-邀请全部(0:是 1:否)"
            +"<br>paper-int(11)-试卷id"
            +"<br>paperName-string-试卷名称"
            +"<br>status-int(11)-状态(0:未发布 1:已发布 2:已结课)"
            +"<br>delete_flag-int(11)-删除标识(0:未删除 1:已删除)"
            +"<br>authorId-int(11)-创建人user表的主键id"
            +"<br>authorName-int(11)-创建人名称"
            +"<br>createTime-yyyy-MM-dd HH:mm:ss-创建时间string"
            +"<br>publishTime-yyyy-MM-dd HH:mm:ss-发布时间string"
            +"<br>updateTime-yyyy-MM-dd HH:mm:ss-修改时间string"
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
        SearchClassDTO searchClassDTO = new SearchClassDTO();
        searchClassDTO.setTypeId(typeId);
        searchClassDTO.setTitle(title);
        searchClassDTO.setPage(page);
        searchClassDTO.setPageSize(pageSize);
        return classService.getPage(searchClassDTO);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("class:add")
    @ApiOperation(value = "根据课程名称title，向课程表里添加一条数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    public Result add(@RequestBody @ApiParam(name = "classDTO", value = "课程表dto") ClassDTO classDTO) {
        return classService.add(classDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("class:update")
    @ApiOperation(value = "根据课程表主键pk_id和无重复的课程名称title，更新课程表里对应id的数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    public Result update(@Valid @RequestBody @ApiParam(name = "classDTO", value = "课程表dto") ClassDTO classDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidateParamException(bindingResult);
        }

        return classService.update(classDTO);
    }

    @PostMapping(value = "/set/user")
    @RequiresPermissions("class:set")
    @ApiOperation(value = "根据课程表的主键pk_id，更新课程表里受邀请用户invite_user的数据", notes = "返回格式"
            +"<br>pkId-int(11)-主键"
            +"<br>typeId-int(11)-分类表的pk_id"
            +"<br>typeName-varchar(32)-名称"
            +"<br>credit-int(11)-学分"
            +"<br>preventionCheat-int(11)-防作弊(0:开启 1:关闭)"
            +"<br>videoDrag-int(11)-拖动视频(0:开启 1:关闭)"
            +"<br>creditAcquisition-int(11)-学分获取(0:完成时获取学分 1:不启用学分)"
            +"<br>studyTime-varchar(64)-开课时间(JSONString)"
            +"<br>picture-varchar(200)-封面图片"
            +"<br>des-text-描述"
            +"<br>classCharge-int(11)-课程收费(0:开启 1:关闭)"
            +"<br>collectNum-int(11)-收藏次数"
            +"<br>upNum-int(11)-点赞次数"
            +"<br>inviteUser-varchar(500)-用户id(,隔开)"
            +"<br>inviteTitle-varchar(500)-标签id(,隔开)"
            +"<br>inviteOrganization-varchar(500)-组织id(,隔开)"
            +"<br>inviteAll-int-邀请全部(0:是 1:否)"
            +"<br>paper-int(11)-试卷id"
            +"<br>paperName-string-试卷名称"
            +"<br>status-int(11)-状态(0:未发布 1:已发布 2:已结课)"
            +"<br>delete_flag-int(11)-删除标识(0:未删除 1:已删除)"
            +"<br>authorId-int(11)-创建人user表的主键id"
            +"<br>authorName-int(11)-创建人名称"
            +"<br>createTime-yyyy-MM-dd HH:mm:ss-创建时间string"
            +"<br>publishTime-yyyy-MM-dd HH:mm:ss-发布时间string"
            +"<br>updateTime-yyyy-MM-dd HH:mm:ss-修改时间string"
    )
    public Result setUser(@RequestBody @ApiParam(name = "classDTO", value = "课程表dto") ClassDTO classDTO) {

        return classService.setUser(classDTO);
    }

    @PostMapping(value = "/set/paper")
    @RequiresPermissions("class:set")
    @ApiOperation(value = "根据课程表的主键pk_id，更新课程表里试卷paper的数据", notes = "返回格式："
            +"<br>pkId-int(11)-主键"
            +"<br>typeId-int(11)-分类表的pk_id"
            +"<br>typeName-varchar(32)-名称"
            +"<br>credit-int(11)-学分"
            +"<br>preventionCheat-int(11)-防作弊(0:开启 1:关闭)"
            +"<br>videoDrag-int(11)-拖动视频(0:开启 1:关闭)"
            +"<br>creditAcquisition-int(11)-学分获取(0:完成时获取学分 1:不启用学分)"
            +"<br>studyTime-varchar(64)-开课时间(JSONString)"
            +"<br>picture-varchar(200)-封面图片"
            +"<br>des-text-描述"
            +"<br>classCharge-int(11)-课程收费(0:开启 1:关闭)"
            +"<br>collectNum-int(11)-收藏次数"
            +"<br>upNum-int(11)-点赞次数"
            +"<br>inviteUser-varchar(500)-用户id(,隔开)"
            +"<br>inviteTitle-varchar(500)-标签id(,隔开)"
            +"<br>inviteOrganization-varchar(500)-组织id(,隔开)"
            +"<br>inviteAll-int-邀请全部(0:是 1:否)"
            +"<br>paper-int(11)-试卷id"
            +"<br>paperName-string-试卷名称"
            +"<br>status-int(11)-状态(0:未发布 1:已发布 2:已结课)"
            +"<br>delete_flag-int(11)-删除标识(0:未删除 1:已删除)"
            +"<br>authorId-int(11)-创建人user表的主键id"
            +"<br>authorName-int(11)-创建人名称"
            +"<br>createTime-yyyy-MM-dd HH:mm:ss-创建时间string"
            +"<br>publishTime-yyyy-MM-dd HH:mm:ss-发布时间string"
            +"<br>updateTime-yyyy-MM-dd HH:mm:ss-修改时间string"
    )
    public Result setPaper(@RequestBody @ApiParam(name = "classDTO", value = "课程表dto") ClassDTO classDTO) {

        return classService.setPaper(classDTO);
    }

    @PostMapping(value = "/status")
    @RequiresPermissions("class:set")
    @ApiOperation(value = "根据课程表的主键pk_id，更新课程状态", notes = "返回格式："
            +"<br>pkId-int(11)-主键"
            +"<br>typeId-int(11)-分类表的pk_id"
            +"<br>typeName-varchar(32)-名称"
            +"<br>credit-int(11)-学分"
            +"<br>preventionCheat-int(11)-防作弊(0:开启 1:关闭)"
            +"<br>videoDrag-int(11)-拖动视频(0:开启 1:关闭)"
            +"<br>creditAcquisition-int(11)-学分获取(0:完成时获取学分 1:不启用学分)"
            +"<br>studyTime-varchar(64)-开课时间(JSONString)"
            +"<br>picture-varchar(200)-封面图片"
            +"<br>des-text-描述"
            +"<br>classCharge-int(11)-课程收费(0:开启 1:关闭)"
            +"<br>collectNum-int(11)-收藏次数"
            +"<br>upNum-int(11)-点赞次数"
            +"<br>inviteUser-varchar(500)-用户id(,隔开)"
            +"<br>inviteTitle-varchar(500)-标签id(,隔开)"
            +"<br>inviteOrganization-varchar(500)-组织id(,隔开)"
            +"<br>inviteAll-int-邀请全部(0:是 1:否)"
            +"<br>paper-int(11)-试卷id"
            +"<br>paperName-string-试卷名称"
            +"<br>status-int(11)-状态(0:未发布 1:已发布 2:已结课)"
            +"<br>delete_flag-int(11)-删除标识(0:未删除 1:已删除)"
            +"<br>authorId-int(11)-创建人user表的主键id"
            +"<br>authorName-int(11)-创建人名称"
            +"<br>createTime-yyyy-MM-dd HH:mm:ss-创建时间string"
            +"<br>publishTime-yyyy-MM-dd HH:mm:ss-发布时间string"
            +"<br>updateTime-yyyy-MM-dd HH:mm:ss-修改时间string"
    )
    public Result setStatus(@RequestBody @ApiParam(name = "classDTO", value = "课程表dto") ClassDTO classDTO) {

        return classService.setStatus(classDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("class:delete")
    @ApiOperation(value = "根据课程表的主键pk_id，批量删除课程", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    @ApiImplicitParam(name = "classIds",value = "int数组-课程表主键pk_id")
    public Result delete(@RequestBody Integer[] classIds) {
        return classService.delete(classIds);
    }
}
