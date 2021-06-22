package com.njbandou.web.service;

import com.njbandou.web.dto.modify.QuestionDTO;
import com.njbandou.web.dto.search.SearchQuestionDTO;
import com.njbandou.web.vo.Result;

public interface QuestionService {
    /**
     * 获取题目列表
     *
     * @param searchQuestionDTO 搜索内容
     * @return PageResult
     */
    Result getPage(SearchQuestionDTO searchQuestionDTO);

    /**
     * 获取随机抽取题目
     *
     * @param searchQuestionDTO 搜索内容
     * @return PageResult
     */
    Result getRandomQuestions(SearchQuestionDTO searchQuestionDTO);

    /**
     * 获取题目数量
     *
     * @return PageResult
     */
    Result getNumber();

    /**
     * 添加题目
     *
     * @param questionDTO 题目参数
     * @return Result
     */
    Result add(QuestionDTO questionDTO);

    /**
     * 更新题目信息
     *
     * @param questionDTO 题目参数
     * @return Result
     */
    Result update(QuestionDTO questionDTO);

    /**
     * 更新题目知识点
     *
     * @param questionDTO 题目参数
     * @return Result
     */
    Result setKnowledgePoint(QuestionDTO questionDTO);

    /**
     * 批量删除题目
     *
     * @param ids 主键数组
     * @return Result
     */
    Result delete(Integer[] ids);
}
