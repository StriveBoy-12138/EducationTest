package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.CatalogDTO;
import com.njbandou.web.dto.search.SearchCatalogDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.CatalogService;
import com.njbandou.web.vo.Result;
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
    public Result getAllCatalog(@RequestParam(required = false, defaultValue = "0") Integer classId) {
        SearchCatalogDTO searchCatalogDTO = new SearchCatalogDTO();
        searchCatalogDTO.setClassId(classId);
        return catalogService.getAll(searchCatalogDTO);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("class:set")
    public Result addCatalog(@Valid @RequestBody CatalogDTO catalogDTO,
                            BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return catalogService.add(catalogDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("class:set")
    public Result updateCatalog(@Valid @RequestBody CatalogDTO catalogDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return catalogService.update(catalogDTO);
    }

    @PostMapping(value = "/move")
    @RequiresPermissions("class:set")
    public Result moveProject(@RequestBody CatalogDTO catalogDTO) {
        return catalogService.move(catalogDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("class:set")
    public Result deleteCatalog(@RequestBody CatalogDTO catalogDTO) {
        return catalogService.delete(catalogDTO);
    }
}
