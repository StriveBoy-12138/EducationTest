package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.KnowledgePointTypeDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.KnowledgePointTypeService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/knowledge/point/type")
@Api(tags = "用于知识点分类的相关接口")
public class KnowledgePointTypeController {
    @Autowired
    private KnowledgePointTypeService knowledgePointTypeService;

    @GetMapping(value = "/list")
    @RequiresPermissions("bank:question:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，获取所有知识点分类信息",notes = "统一返回：\n" +
            "pkId-知识点分类主键\n" +
            "name-知识点分类名称\n" )
    public Result getAllKnowledgePointType() {
        return knowledgePointTypeService.getAll();
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("bank:question:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，添加知识点分类信息",notes = "返回“操作成功”" )
    public Result addKnowledgePointType(@Valid @RequestBody KnowledgePointTypeDTO knowledgePointTypeDTO,
                            BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return knowledgePointTypeService.add(knowledgePointTypeDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("bank:question:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，修改知识点分类信息",notes = "返回“操作成功”" )
    public Result updateKnowledgePointType(@Valid @RequestBody KnowledgePointTypeDTO knowledgePointTypeDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return knowledgePointTypeService.update(knowledgePointTypeDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("bank:question:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，删除知识点分类信息",notes = "返回“操作成功”" )
    public Result deleteKnowledgePointType(@RequestBody KnowledgePointTypeDTO knowledgePointTypeDTO) {
        return knowledgePointTypeService.delete(knowledgePointTypeDTO);
    }
}
