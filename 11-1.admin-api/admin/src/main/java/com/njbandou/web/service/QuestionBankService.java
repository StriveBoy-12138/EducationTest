package com.njbandou.web.service;

import com.njbandou.web.dto.modify.QuestionBankDTO;
import com.njbandou.web.dto.search.SearchQuestionBankDTO;
import com.njbandou.web.vo.Result;

public interface QuestionBankService {
    /**
     * 获取题库列表
     *
     * @param searchQuestionBankDTO 搜索内容
     * @return PageResult
     */
    Result getPage(SearchQuestionBankDTO searchQuestionBankDTO);

    /**
     * 添加题库
     *
     * @param questionBankDTO 题库参数
     * @return Result
     */
    Result add(QuestionBankDTO questionBankDTO);

    /**
     * 更新题库信息
     *
     * @param questionBankDTO 题库参数
     * @return Result
     */
    Result update(QuestionBankDTO questionBankDTO);

    /**
     * 批量删除题库
     *
     * @param ids 主键数组
     * @return Result
     */
    Result delete(Integer[] ids);
}
