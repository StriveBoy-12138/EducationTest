package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.InformationCollectionDTO;
import com.njbandou.web.dto.search.SearchInformationCollectionDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.InformationCollectionService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/information/collection")
public class InformationCollectionController {
    @Autowired
    private InformationCollectionService informationCollectionService;

    @GetMapping(value = "/list")
    @RequiresPermissions("information:collection")
    @ApiOperation(value = "根据关联relationId, 获取所有信息采集项", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回内容:"+
    "informationCollection.pkId," +
            "<br>relationId,关联id" +
            "<br>type,信息类型," +
            "<br>title,信息名称" +
            "<br>isRequired,是否必填(0:是 1:否)" +
            "<br>isAble,是否可填/唯一(0:是 1:否)" +
            "<br>option,选项" +
            "<br>sort,排序用int;")
    @ApiImplicitParam(name = "relationId", value = "",defaultValue = "0",required = false)
    public Result getAllInformationCollection(@RequestParam(required = false, defaultValue = "0") Integer relationId) {
        SearchInformationCollectionDTO searchInformationCollectionDTO = new SearchInformationCollectionDTO();
        searchInformationCollectionDTO.setRelationId(relationId);
        return informationCollectionService.getAll(searchInformationCollectionDTO);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("information:collection")
    @ApiOperation(value = "根据关联relationId和信息名称, 添加一项信息采集项", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    public Result addInformationCollection(@Valid @RequestBody @ApiParam(name = "informationCollectionDTO", value =
            "信息收集dto") InformationCollectionDTO informationCollectionDTO,
           BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return informationCollectionService.add(informationCollectionDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("information:collection")
    @ApiOperation(value = "根据信息采集主键pkId和非重复的关联relationId和信息名称，更新一项信息采集项", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数," +
            "data返回简单提示:")
    public Result updateInformationCollection(@Valid @RequestBody @ApiParam(name = "informationCollectionDTO", value =
            "信息收集dto") InformationCollectionDTO informationCollectionDTO,
              BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return informationCollectionService.update(informationCollectionDTO);
    }

    @PostMapping(value = "/move")
    @RequiresPermissions("information:collection")
    @ApiOperation(value = "根据信息采集主键pkId和非重复的关联relationId和信息名称，上移下移一项信息采集项", notes = "code-{0-成功,1-失败},msg-结果说明," +
            "count-数据条数," +
            "data返回简单提示:")
    public Result moveProject(@RequestBody  @ApiParam(name = "informationCollectionDTO", value =
            "信息收集dto")InformationCollectionDTO informationCollectionDTO) {
        return informationCollectionService.move(informationCollectionDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("information:collection")
    @ApiOperation(value = "根据信息采集主键pkId，更新一项信息采集项", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数," +
            "data返回简单提示:")
    public Result deleteInformationCollection(@RequestBody  @ApiParam(name = "informationCollectionDTO", value =
            "信息收集dto")InformationCollectionDTO informationCollectionDTO) {
        return informationCollectionService.delete(informationCollectionDTO);
    }
}
