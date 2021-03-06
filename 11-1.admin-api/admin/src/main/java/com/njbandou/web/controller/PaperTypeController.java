package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.PaperTypeDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.PaperTypeService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/paper/type")
@Api(tags = "用于试卷分类的相关接口")
public class PaperTypeController {
    @Autowired
    private PaperTypeService paperTypeService;

    @GetMapping(value = "/list")
    @RequiresPermissions("paper:type:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，获取所有题库分类信息",notes = "统一返回：\n" +
            "pkId-试卷分类主键\n" +
            "name-试卷分类名称\n" )
    public Result getAllPaperType() {
        return paperTypeService.getAll();
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("paper:type:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，添加试卷分类信息",notes = "返回“操作成功”" )
    public Result addPaperType(@Valid @RequestBody PaperTypeDTO paperTypeDTO,
                            BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return paperTypeService.add(paperTypeDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("paper:type:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，修改试卷分类信息",notes = "返回“操作成功”" )
    public Result updatePaperType(@Valid @RequestBody PaperTypeDTO paperTypeDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return paperTypeService.update(paperTypeDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("paper:type:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，删除试卷分类信息",notes = "返回“操作成功”" )
    public Result deletePaperType(@RequestBody PaperTypeDTO paperTypeDTO) {
        return paperTypeService.delete(paperTypeDTO);
    }
}
