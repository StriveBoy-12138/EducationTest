package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.QuestionBankTypeDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.QuestionBankTypeService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/question/bank/type")
@Api(tags = "用于题库分类的相关接口")
public class QuestionBankTypeController {
    @Autowired
    private QuestionBankTypeService questionBankTypeService;

    @GetMapping(value = "/list")
    @RequiresPermissions("bank:type:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，获取所有题库分类信息",notes = "统一返回：\n" +
            "pkId-题库分类主键\n" +
            "name-题库分类名称\n" )
    public Result getAllQuestionBankType() {
        return questionBankTypeService.getAll();
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("bank:type:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，添加题库分类信息",notes = "返回“操作成功”" )
    public Result addQuestionBankType(@Valid @RequestBody QuestionBankTypeDTO questionBankTypeDTO,
                            BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return questionBankTypeService.add(questionBankTypeDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("bank:type:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，修改题库分类信息",notes = "返回“操作成功”" )
    public Result updateQuestionBankType(@Valid @RequestBody QuestionBankTypeDTO questionBankTypeDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return questionBankTypeService.update(questionBankTypeDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("bank:type:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，删除题库分类信息",notes = "返回“操作成功”" )
    public Result deleteQuestionBankType(@RequestBody QuestionBankTypeDTO questionBankTypeDTO) {
        return questionBankTypeService.delete(questionBankTypeDTO);
    }
}
