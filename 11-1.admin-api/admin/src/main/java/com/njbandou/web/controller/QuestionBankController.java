package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.QuestionBankDTO;
import com.njbandou.web.dto.search.SearchQuestionBankDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.QuestionBankService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/question/bank")
@Api(tags = "用于题库信息的相关接口")
public class QuestionBankController {
    @Autowired
    private QuestionBankService questionBankService;

    @GetMapping(value = "/list")
    @RequiresPermissions("bank:list")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，分页获取题库信息",notes = "统一返回：\n" +
            "pkId-题库主键\n" +
            "typeId-题库分类id" +
            "typeName-题库分类名称" +
            "name-题库名称" +
            "des-题库描述" +
            "picture-图片" +
            "authorId-创建人id" +
            "authorName-创建人名称" +
            "createTime-创建时间" )
    public Result list(@RequestParam(required = false, defaultValue = "0") Integer typeId,
                       @RequestParam(required = false, defaultValue = "") String name,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false, defaultValue = "1") Integer page) {
        SearchQuestionBankDTO searchQuestionBankDTO = new SearchQuestionBankDTO();
        searchQuestionBankDTO.setTypeId(typeId);
        searchQuestionBankDTO.setName(name);
        searchQuestionBankDTO.setPage(page);
        searchQuestionBankDTO.setPageSize(pageSize);
        return questionBankService.getPage(searchQuestionBankDTO);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("bank:add")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，添加题库信息",notes = "返回“操作成功”" )
    public Result add(@RequestBody QuestionBankDTO questionBankDTO) {
        return questionBankService.add(questionBankDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("bank:update")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，修改题库信息",notes = "返回“操作成功”" )
    public Result update(@Valid @RequestBody QuestionBankDTO questionBankDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidateParamException(bindingResult);
        }

        return questionBankService.update(questionBankDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("bank:delete")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，删除题库信息",notes = "返回“操作成功”" )
    public Result delete(@RequestBody Integer[] questionBankIds) {
        return questionBankService.delete(questionBankIds);
    }
}
