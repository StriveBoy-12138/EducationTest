package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.KnowledgePointTypeDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.KnowledgePointTypeService;
import com.njbandou.web.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/knowledge/point/type")
public class KnowledgePointTypeController {
    @Autowired
    private KnowledgePointTypeService knowledgePointTypeService;

    @GetMapping(value = "/list")
    @RequiresPermissions("bank:question:manage")
    public Result getAllKnowledgePointType() {
        return knowledgePointTypeService.getAll();
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("bank:question:manage")
    public Result addKnowledgePointType(@Valid @RequestBody KnowledgePointTypeDTO knowledgePointTypeDTO,
                            BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return knowledgePointTypeService.add(knowledgePointTypeDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("bank:question:manage")
    public Result updateKnowledgePointType(@Valid @RequestBody KnowledgePointTypeDTO knowledgePointTypeDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return knowledgePointTypeService.update(knowledgePointTypeDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("bank:question:manage")
    public Result deleteKnowledgePointType(@RequestBody KnowledgePointTypeDTO knowledgePointTypeDTO) {
        return knowledgePointTypeService.delete(knowledgePointTypeDTO);
    }
}
