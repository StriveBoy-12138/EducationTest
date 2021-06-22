package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.ExaminationDTO;
import com.njbandou.web.service.ExaminationService;
import com.njbandou.web.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/examination")
public class ExaminationController {
    @Autowired
    private ExaminationService examinationService;

    @GetMapping(value = "/get")
    @RequiresPermissions("paper:set")
    public Result get(@RequestParam(required = false, defaultValue = "0") Integer paperId) {
        return examinationService.get(paperId);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("paper:set")
    public Result update(@RequestBody ExaminationDTO examinationDTO) {
        return examinationService.update(examinationDTO);
    }
}
