package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.CatalogCoursewareDTO;
import com.njbandou.web.dto.search.SearchCatalogCoursewareDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.CatalogCoursewareService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/catalog/courseware")
@Api("测试info")
public class CatalogCoursewareController {
    @Autowired
    private CatalogCoursewareService catalogCoursewareService;

    @GetMapping(value = "/list")
    @RequiresPermissions("class:set")
    @ApiOperation(value = "根据目录主键映射的catalogId，获取目录课件表里对应目录id的所有数据",notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回内容:" +
            "pkId-目录课件表主键pk_id<br>" +
            "catalogId-目录表主键pk_id<br>" +
            "coursewareId-课件表主键pk_id<br>" +
            "coursewareId-课件表主键pk_id<br>" +
            "coursewareTitle-课件名称<br>"+
            "studyTime-学习时长<br>"+
            "commentTime-评论次数<br>"+
            "sort-int类型用于排序<br>")
    @ApiImplicitParam(name = "catalogId",value = "目录表id-即目录表的主键pk_id",defaultValue = "0",required=false)
    public Result getAllCatalogCourseware(@RequestParam(required = false, defaultValue = "0") Integer catalogId) {
        SearchCatalogCoursewareDTO searchCatalogCoursewareDTO = new SearchCatalogCoursewareDTO();
        searchCatalogCoursewareDTO.setCatalogId(catalogId);
        return catalogCoursewareService.getAll(searchCatalogCoursewareDTO);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("class:set")
    @ApiOperation(value = "根据目录主键映射的catalogId和课件coursewareId，向目录课件表里添加(可批量)对应目录课件的数据",notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    public Result addCatalog(@Valid @RequestBody @ApiParam(name = "目录课件dto列表",value = "目录课件dto",
            required = true) List<@Valid CatalogCoursewareDTO> catalogCoursewareList,
                             BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return catalogCoursewareService.add(catalogCoursewareList);
    }

    @PostMapping(value = "/move")
    @RequiresPermissions("class:set")
    @ApiOperation(value = "根据目录主键映射的catalogId和课件coursewareId，下移上移目录课件，up-上移down-下移",notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    public Result moveCatalogCourseware(@RequestBody @ApiParam(name = "目录课件dto列表",value = "目录课件dto",
            required = true) CatalogCoursewareDTO catalogCoursewareDTO) {
        return catalogCoursewareService.move(catalogCoursewareDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("class:set")
    @ApiOperation(value = "根据目录课件表主键pkId，删除目录课件表里对应目录课件的数据",notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回内容:")
    public Result deleteCatalogCourseware(@RequestBody @ApiParam(name = "目录课件dto列表",value = "目录课件dto",
            required = true)CatalogCoursewareDTO catalogCoursewareDTO) {
        return catalogCoursewareService.delete(catalogCoursewareDTO);
    }
}
