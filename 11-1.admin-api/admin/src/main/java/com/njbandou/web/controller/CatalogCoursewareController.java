package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.CatalogCoursewareDTO;
import com.njbandou.web.dto.search.SearchCatalogCoursewareDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.CatalogCoursewareService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "desc of method", notes = "")
    public Result getAllCatalogCourseware(@RequestParam(required = false, defaultValue = "0") Integer catalogId) {
        SearchCatalogCoursewareDTO searchCatalogCoursewareDTO = new SearchCatalogCoursewareDTO();
        searchCatalogCoursewareDTO.setCatalogId(catalogId);
        return catalogCoursewareService.getAll(searchCatalogCoursewareDTO);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("class:set")
    public Result addCatalog(@Valid @RequestBody List<@Valid CatalogCoursewareDTO> catalogCoursewareList,
                             BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return catalogCoursewareService.add(catalogCoursewareList);
    }

    @PostMapping(value = "/move")
    @RequiresPermissions("class:set")
    public Result moveCatalogCourseware(@RequestBody CatalogCoursewareDTO catalogCoursewareDTO) {
        return catalogCoursewareService.move(catalogCoursewareDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("class:set")
    public Result deleteCatalogCourseware(@RequestBody CatalogCoursewareDTO catalogCoursewareDTO) {
        return catalogCoursewareService.delete(catalogCoursewareDTO);
    }
}
