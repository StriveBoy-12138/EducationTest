package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.KnowledgePointDTO;
import com.njbandou.web.dto.search.SearchKnowledgePointDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.KnowledgePointService;
import com.njbandou.web.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/knowledge/point")
public class KnowledgePointController {
    @Autowired
    private KnowledgePointService knowledgePointService;

    @GetMapping(value = "/list")
    @RequiresPermissions("bank:question:manage")
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
    public Result add(@RequestBody KnowledgePointDTO knowledgePointDTO) {
        return knowledgePointService.add(knowledgePointDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("bank:question:manage")
    public Result update(@Valid @RequestBody KnowledgePointDTO knowledgePointDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidateParamException(bindingResult);
        }

        return knowledgePointService.update(knowledgePointDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("bank:question:manage")
    public Result delete(@RequestBody Integer[] knowledgePointIds) {
        return knowledgePointService.delete(knowledgePointIds);
    }
}
