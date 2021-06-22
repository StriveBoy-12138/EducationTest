package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.PaperTypeDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.PaperTypeService;
import com.njbandou.web.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/paper/type")
public class PaperTypeController {
    @Autowired
    private PaperTypeService paperTypeService;

    @GetMapping(value = "/list")
    @RequiresPermissions("paper:type:manage")
    public Result getAllPaperType() {
        return paperTypeService.getAll();
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("paper:type:manage")
    public Result addPaperType(@Valid @RequestBody PaperTypeDTO paperTypeDTO,
                            BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return paperTypeService.add(paperTypeDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("paper:type:manage")
    public Result updatePaperType(@Valid @RequestBody PaperTypeDTO paperTypeDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return paperTypeService.update(paperTypeDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("paper:type:manage")
    public Result deletePaperType(@RequestBody PaperTypeDTO paperTypeDTO) {
        return paperTypeService.delete(paperTypeDTO);
    }
}
