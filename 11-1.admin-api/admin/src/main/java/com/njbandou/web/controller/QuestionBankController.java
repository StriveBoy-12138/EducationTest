package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.QuestionBankDTO;
import com.njbandou.web.dto.search.SearchQuestionBankDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.QuestionBankService;
import com.njbandou.web.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/question/bank")
public class QuestionBankController {
    @Autowired
    private QuestionBankService questionBankService;

    @GetMapping(value = "/list")
    @RequiresPermissions("bank:list")
    public Result list(@RequestParam(required = false, defaultValue = "0") Integer typeId,
                       @RequestParam(required = false, defaultValue = "") String name,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false, defaultValue = "1") Integer page) {
        SearchQuestionBankDTO searchQuestionBankDTO = new SearchQuestionBankDTO();
        searchQuestionBankDTO.setTypeId(typeId);
        searchQuestionBankDTO.setName(name);
        searchQuestionBankDTO.setPage(page);
        searchQuestionBankDTO.setPageSize(pageSize);
        return questionBankService.getPage(searchQuestionBankDTO);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("bank:add")
    public Result add(@RequestBody QuestionBankDTO questionBankDTO) {
        return questionBankService.add(questionBankDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("bank:update")
    public Result update(@Valid @RequestBody QuestionBankDTO questionBankDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidateParamException(bindingResult);
        }

        return questionBankService.update(questionBankDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("bank:delete")
    public Result delete(@RequestBody Integer[] questionBankIds) {
        return questionBankService.delete(questionBankIds);
    }
}
