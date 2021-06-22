package com.njbandou.web.service;

import com.njbandou.web.dto.modify.QuestionBankTypeDTO;
import com.njbandou.web.vo.Result;

public interface QuestionBankTypeService {
    /**
     * 获取题库分类列表
     *
     * @return PageResult
     */
    Result getAll();

    /**
     * 添加题库分类
     *
     * @param questionBankTypeDTO 题库分类参数
     * @return Result
     */
    Result add(QuestionBankTypeDTO questionBankTypeDTO);

    /**
     * 更新题库分类信息
     *
     * @param questionBankTypeDTO 题库分类参数
     * @return Result
     */
    Result update(QuestionBankTypeDTO questionBankTypeDTO);

    /**
     * 删除题库分类
     *
     * @param questionBankTypeDTO 主键
     * @return Result
     */
    Result delete(QuestionBankTypeDTO questionBankTypeDTO);
}
