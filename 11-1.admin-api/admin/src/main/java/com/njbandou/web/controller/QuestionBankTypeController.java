package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.QuestionBankTypeDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.QuestionBankTypeService;
import com.njbandou.web.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/question/bank/type")
public class QuestionBankTypeController {
    @Autowired
    private QuestionBankTypeService questionBankTypeService;

    @GetMapping(value = "/list")
    @RequiresPermissions("bank:type:manage")
    public Result getAllQuestionBankType() {
        return questionBankTypeService.getAll();
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("bank:type:manage")
    public Result addQuestionBankType(@Valid @RequestBody QuestionBankTypeDTO questionBankTypeDTO,
                            BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return questionBankTypeService.add(questionBankTypeDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("bank:type:manage")
    public Result updateQuestionBankType(@Valid @RequestBody QuestionBankTypeDTO questionBankTypeDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidateParamException(result);
        }
        return questionBankTypeService.update(questionBankTypeDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("bank:type:manage")
    public Result deleteQuestionBankType(@RequestBody QuestionBankTypeDTO questionBankTypeDTO) {
        return questionBankTypeService.delete(questionBankTypeDTO);
    }
}
