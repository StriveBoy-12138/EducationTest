package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.CatalogDTO;
import com.njbandou.web.dto.search.SearchCatalogDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.CatalogService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/catalog")
public class CatalogController {
    @Autowired
    private CatalogService catalogService;

    @GetMapping(value = "/list")
    @RequiresPermissions("class:set")
    @ApiOperation(value = "shiro注解方式做权限校验通过后，获取目录表里所有的数据",notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回内容:" +
            "pkId-目录表主键pk_id<br>" +
            "classId-课程表主键pk_id<br>" +
            "title-目录名称<br>"+
            "sort-int类型用于排序<br>")
    @ApiImplicitParam(name = "classId",value = "课程id-课程表的主键pk_id",defaultValue = "0",required=false)
    public Result getAllCatalog(@RequestParam(required = false, defaultValue = "0") Integer classId) {
        SearchCatalogDTO searchCatalogDTO = new SearchCatalogDTO();
        searchCatalogDTO.setClassId(classId);
        return catalogService.getAll(searchCatalogDTO);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("class:set")
    @ApiOperation(value = "根据课程表主键映射的classId和课程名称title，向目录表添加一条目录数据，课程表id为空则报错，其他数据自动填入",notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    public Result addCatalog(@Valid @RequestBody @ApiParam(name = "目录表单条数据dto",value = "目录表单条数据dto",required = true)CatalogDTO catalogDTO,
                            BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return catalogService.add(catalogDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("class:set")
    @ApiOperation(value = "根据课程表主键映射的classId和课程名称title，更新目录表的一条目录数据",notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    public Result updateCatalog(@Valid @RequestBody @ApiParam(name = "目录表单条数据dto",value = "目录表单条数据dto",required = true)CatalogDTO catalogDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return catalogService.update(catalogDTO);
    }

    @PostMapping(value = "/move")
    @RequiresPermissions("class:set")
    @ApiOperation(value = "根据课程表主键映射的classId，下移上移目录",notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    public Result moveProject(@RequestBody @ApiParam(name = "目录表单条数据dto",value = "目录表单条数据dto",required = true)CatalogDTO catalogDTO) {
        return catalogService.move(catalogDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("class:set")
    @ApiOperation(value = "根据目录表主键pkId，真删除目录表的一条目录数据",notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    public Result deleteCatalog(@RequestBody @ApiParam(name = "目录表单条数据dto",value = "目录表单条数据dto",required = true)CatalogDTO catalogDTO) {
        return catalogService.delete(catalogDTO);
    }
}
