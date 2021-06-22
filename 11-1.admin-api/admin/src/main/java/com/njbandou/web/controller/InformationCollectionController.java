package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.InformationCollectionDTO;
import com.njbandou.web.dto.search.SearchInformationCollectionDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.InformationCollectionService;
import com.njbandou.web.vo.Result;
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
    public Result getAllInformationCollection(@RequestParam(required = false, defaultValue = "0") Integer relationId) {
        SearchInformationCollectionDTO searchInformationCollectionDTO = new SearchInformationCollectionDTO();
        searchInformationCollectionDTO.setRelationId(relationId);
        return informationCollectionService.getAll(searchInformationCollectionDTO);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("information:collection")
    public Result addInformationCollection(@Valid @RequestBody InformationCollectionDTO informationCollectionDTO,
                           BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return informationCollectionService.add(informationCollectionDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("information:collection")
    public Result updateInformationCollection(@Valid @RequestBody InformationCollectionDTO informationCollectionDTO,
                              BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return informationCollectionService.update(informationCollectionDTO);
    }

    @PostMapping(value = "/move")
    @RequiresPermissions("information:collection")
    public Result moveProject(@RequestBody InformationCollectionDTO informationCollectionDTO) {
        return informationCollectionService.move(informationCollectionDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("information:collection")
    public Result deleteInformationCollection(@RequestBody InformationCollectionDTO informationCollectionDTO) {
        return informationCollectionService.delete(informationCollectionDTO);
    }
}
