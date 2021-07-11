package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.QuestionDTO;
import com.njbandou.web.dto.search.SearchQuestionDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.QuestionService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/question")
@Api(tags = "用于题目管理的相关接口")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping(value = "/list")
    @RequiresPermissions("bank:question:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，分页获取题目信息",notes = "统一返回：\n" +
            "pkId-题目主键\n" +
            "bankId-题库id" +
            "questionId-题目id" +
            "type-题目分类" +
            "title-题目信息" +
            "knowledgePoint-知识点id(,隔开)" +
            "audioName-音频名称" +
            "audio-音频" +
            "videoName-视频名称" +
            "video-视频" +
            "fileName-文档名称(,隔开)" +
            "file-文档(,隔开)" +
            "analysis-解析" +
            "analysisAudioName-解析音频名称" +
            "analysisAudio-解析音频" +
            "analysisVideoName-解析视频名称" +
            "analysisVideo-解析视频" +
            "answer-答案(JSONString)" +
            "difficulty-难度(0:高 1:中 2:低)" +
            "createTime-创建时间" +
            "updateTime-修改时间" )
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
    @ApiOperation(value = "shiro注解方式做权限检验通过后，获取随机题目",notes = "返回“操作成功”" )
    public Result getRandomQuestions(@RequestBody SearchQuestionDTO searchQuestionDTO) {
        return questionService.getRandomQuestions(searchQuestionDTO);
    }

    @GetMapping(value = "/number")
    @RequiresPermissions("bank:question:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，获取题目数目",notes = "统一返回：\n" +
            "type数组：各个type数目" +
            "difficulty数组：各个difficult数目" )
    public Result getNumber() {
        return questionService.getNumber();
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("bank:question:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，添加题目",notes = "返回“操作成功”" )
    public Result add(@RequestBody QuestionDTO questionDTO) {
        return questionService.add(questionDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("bank:question:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，修改题目",notes = "返回“操作成功”" )
    public Result update(@Valid @RequestBody QuestionDTO questionDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidateParamException(bindingResult);
        }

        return questionService.update(questionDTO);
    }

    @PostMapping(value = "/set/knowledgePoint")
    @RequiresPermissions("bank:question:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，设置知识点id",notes = "返回“操作成功”" )
    public Result setKnowledgePoint(@RequestBody QuestionDTO questionDTO) {
        return questionService.setKnowledgePoint(questionDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("bank:question:manage")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，删除题目",notes = "返回“操作成功”" )
    public Result delete(@RequestBody Integer[] questionIds) {
        return questionService.delete(questionIds);
    }
}
