package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.QuestionDTO;
import com.njbandou.web.dto.search.SearchQuestionDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.QuestionService;
import com.njbandou.web.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping(value = "/list")
    @RequiresPermissions("bank:question:manage")
    public Result list(@RequestParam(required = false, defaultValue = "0") Integer bankId,
                       @RequestParam(required = false, defaultValue = "0") Integer questionId,
                       @RequestParam(required = false, defaultValue = "-1") Integer type,
                       @RequestParam(required = false, defaultValue = "-1") Integer difficulty,
                       @RequestParam(required = false, defaultValue = "") String title,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false, defaultValue = "1") Integer page) {
        SearchQuestionDTO searchQuestionDTO = new SearchQuestionDTO();
        searchQuestionDTO.setBankId(bankId);
        searchQuestionDTO.setQuestionId(questionId);
        searchQuestionDTO.setType(type);
        searchQuestionDTO.setDifficulty(difficulty);
        searchQuestionDTO.setTitle(title);
        searchQuestionDTO.setPage(page);
        searchQuestionDTO.setPageSize(pageSize);
        return questionService.getPage(searchQuestionDTO);
    }

    @PostMapping(value = "/random")
    @RequiresPermissions("bank:question:manage")
    public Result getRandomQuestions(@RequestBody SearchQuestionDTO searchQuestionDTO) {
        return questionService.getRandomQuestions(searchQuestionDTO);
    }

    @GetMapping(value = "/number")
    @RequiresPermissions("bank:question:manage")
    public Result getNumber() {
        return questionService.getNumber();
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("bank:question:manage")
    public Result add(@RequestBody QuestionDTO questionDTO) {
        return questionService.add(questionDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("bank:question:manage")
    public Result update(@Valid @RequestBody QuestionDTO questionDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidateParamException(bindingResult);
        }

        return questionService.update(questionDTO);
    }

    @PostMapping(value = "/set/knowledgePoint")
    @RequiresPermissions("bank:question:manage")
    public Result setKnowledgePoint(@RequestBody QuestionDTO questionDTO) {
        return questionService.setKnowledgePoint(questionDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("bank:question:manage")
    public Result delete(@RequestBody Integer[] questionIds) {
        return questionService.delete(questionIds);
    }
}
