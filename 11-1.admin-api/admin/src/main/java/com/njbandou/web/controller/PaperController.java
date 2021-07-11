package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.PaperDTO;
import com.njbandou.web.dto.search.SearchPaperDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.PaperService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/paper")
@Api(tags = "用于题库信息的相关接口")
public class PaperController {
    @Autowired
    private PaperService paperService;

    @GetMapping(value = "/list")
    @RequiresPermissions("paper:list")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，分页获取试卷信息",notes = "统一返回：\n" +
            "pkId-试卷主键\n" +
            "testedTime-考试时间" +
            "inExam-是否考试中" +
            "typeId-试卷分类id" +
            "typeName-试卷分类名称" +
            "title-名称" +
            "des-描述" +
            "isOpen-是否公开(0:是 1:否)" +
            "isFixed-是否固定(0:是 1:否)" +
            "questions-题目(JSONString)" +
            "questionRule-选题规则(题目随机时)(JSONString)" +
            "status-状态(0:待发布 1:已发布)" +
            "authorId-创建人" +
            "authorName-创建人名称" +
            "placeFile-归档(0:未归档 1:已归档)" +
            "inviteUser-用户id(,隔开)" +
            "inviteTitle-标签id(,隔开)" +
            "inviteOrganization-组织id(,隔开)" +
            "inviteAll-邀请全部(0:是 1:否)" +
            "updateTime-修改时间" +
            "createTime-创建时间" )
    public Result list(@RequestParam(required = false, defaultValue = "0") Integer placeFile,
                       @RequestParam(required = false, defaultValue = "0") Integer typeId,
                       @RequestParam(required = false, defaultValue = "-1") Integer status,
                       @RequestParam(required = false, defaultValue = "-1") Integer isOpen,
                       @RequestParam(required = false, defaultValue = "-1") Integer isFixed,
                       @RequestParam(required = false, defaultValue = "") String title,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false, defaultValue = "1") Integer page) {
        SearchPaperDTO searchPaperDTO = new SearchPaperDTO();
        searchPaperDTO.setPlaceFile(placeFile);
        searchPaperDTO.setTypeId(typeId);
        searchPaperDTO.setTitle(title);
        searchPaperDTO.setStatus(status);
        searchPaperDTO.setIsOpen(isOpen);
        searchPaperDTO.setIsFixed(isFixed);
        searchPaperDTO.setPage(page);
        searchPaperDTO.setPageSize(pageSize);
        return paperService.getPage(searchPaperDTO);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("paper:add")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，添加试卷",notes = "返回“操作成功”" )
    public Result add(@Valid @RequestBody PaperDTO paperDTO,
                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidateParamException(bindingResult);
        }
        return paperService.add(paperDTO);
    }

    @GetMapping(value = "/get")
    @RequiresPermissions("paper:list")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，通过id获取试卷",notes = "统一返回：\n" +
            "pkId-试卷主键\n" +
            "testedTime-考试时间" +
            "inExam-是否考试中" +
            "typeId-试卷分类id" +
            "typeName-试卷分类名称" +
            "title-名称" +
            "des-描述" +
            "isOpen-是否公开(0:是 1:否)" +
            "isFixed-是否固定(0:是 1:否)" +
            "questions-题目(JSONString)" +
            "questionRule-选题规则(题目随机时)(JSONString)" +
            "status-状态(0:待发布 1:已发布)" +
            "authorId-创建人" +
            "authorName-创建人名称" +
            "placeFile-归档(0:未归档 1:已归档)" +
            "inviteUser-用户id(,隔开)" +
            "inviteTitle-标签id(,隔开)" +
            "inviteOrganization-组织id(,隔开)" +
            "inviteAll-邀请全部(0:是 1:否)" +
            "updateTime-修改时间" +
            "createTime-创建时间")
    @ApiImplicitParam(name = "paperId",value = "试卷id")
            public Result get(@RequestParam(required = false, defaultValue = "0") Integer paperId) {
        return paperService.getPaper(paperId);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("paper:update")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，修改试卷",notes = "返回“操作成功”" )
    public Result update(@Valid @RequestBody PaperDTO paperDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidateParamException(bindingResult);
        }

        return paperService.update(paperDTO);
    }

    @PostMapping(value = "/set/status")
    @RequiresPermissions("paper:status")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，设置试卷状态（0:待发布 1:已发布）",notes = "返回“操作成功”" )
    public Result setStatus(@RequestBody PaperDTO paperDTO) {
        return paperService.setStatus(paperDTO);
    }

    @PostMapping(value = "/set/user")
    @RequiresPermissions("paper:set")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，设置试卷邀请用户",notes = "返回“操作成功”" )
    public Result setUser(@RequestBody PaperDTO paperDTO) {

        return paperService.setUser(paperDTO);
    }

    @PostMapping(value = "/set/questions")
    @RequiresPermissions("paper:set")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，设置试卷问题",notes = "返回“操作成功”" )
    public Result setQuestions(@RequestBody PaperDTO paperDTO) {

        return paperService.setQuestions(paperDTO);
    }

    @PostMapping(value = "/set/rules")
    @RequiresPermissions("paper:set")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，设置试卷选题规则(题目随机时)(JSONString)",notes = "返回“操作成功”" )
    public Result setRules(@RequestBody PaperDTO paperDTO) {

        return paperService.setRules(paperDTO);
    }

    @PostMapping(value = "/placeFile")
    @RequiresPermissions("paper:place")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，设置试卷归档(0:未归档 1:已归档)",notes = "返回“操作成功”" )
    public Result placeFilePaper(@RequestBody PaperDTO paperDTO) {

        return paperService.placeFilePaper(paperDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("paper:delete")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，删除题目",notes = "返回“操作成功”" )
    public Result delete(@RequestBody Integer[] paperIds) {
        return paperService.delete(paperIds);
    }
}
