package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.KnowledgePointDTO;
import com.njbandou.web.dto.search.SearchKnowledgePointDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.KnowledgePointService;
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
@RequestMapping("/knowledge/point")
@Api(tags = "用于知识点信息的相关接口")
public class KnowledgePointController {
    @Autowired
    private KnowledgePointService knowledgePointService;

    @GetMapping(value = "/list")
    @RequiresPermissions("bank:question:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，分页获取知识点信息",notes = "统一返回：\n" +
            "pkId-知识点主键" +
            "typeName-知识点分类" +
            "name-名称" +
            "authorId-创建人" +
            "authorName-创建人名称" +
            "createTime-创建时间")
    public Result list(@RequestParam(required = false, name = "exceptKnowledgePointIds[]") Integer[] exceptKnowledgePointIds,
                       @RequestParam(required = false, defaultValue = "0") Integer typeId,
                       @RequestParam(required = false, defaultValue = "") String name,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false, defaultValue = "1") Integer page) {
        SearchKnowledgePointDTO searchKnowledgePointDTO = new SearchKnowledgePointDTO();
        searchKnowledgePointDTO.setExceptKnowledgePointIds(exceptKnowledgePointIds);
        searchKnowledgePointDTO.setTypeId(typeId);
        searchKnowledgePointDTO.setName(name);
        searchKnowledgePointDTO.setPage(page);
        searchKnowledgePointDTO.setPageSize(pageSize);
        return knowledgePointService.getPage(searchKnowledgePointDTO);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("bank:question:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，添加知识点",notes = "返回“操作成功”" )
    public Result add(@RequestBody KnowledgePointDTO knowledgePointDTO) {
        return knowledgePointService.add(knowledgePointDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("bank:question:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，修改知识点",notes = "返回“操作成功”" )
    public Result update(@Valid @RequestBody KnowledgePointDTO knowledgePointDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidateParamException(bindingResult);
        }

        return knowledgePointService.update(knowledgePointDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("bank:question:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，删除知识点",notes = "返回“操作成功”" )
    @ApiImplicitParam(name = "knowledgePointIds",value = "知识点id")
    public Result delete(@RequestBody Integer[] knowledgePointIds) {
        return knowledgePointService.delete(knowledgePointIds);
    }
}
