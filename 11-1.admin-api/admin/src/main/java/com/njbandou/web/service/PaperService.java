package com.njbandou.web.service;

import com.njbandou.web.dto.modify.PaperDTO;
import com.njbandou.web.dto.search.SearchPaperDTO;
import com.njbandou.web.vo.Result;

public interface PaperService {
    /**
     * 获取试卷列表
     *
     * @param searchPaperDTO 搜索内容
     * @return PageResult
     */
    Result getPage(SearchPaperDTO searchPaperDTO);

    /**
     * 获取试卷
     *
     * @param paperId 搜索内容
     * @return PageResult
     */
    Result getPaper(Integer paperId);

    /**
     * 添加试卷
     *
     * @param paperDTO 试卷参数
     * @return Result
     */
    Result add(PaperDTO paperDTO);

    /**
     * 更新试卷信息
     *
     * @param paperDTO 试卷参数
     * @return Result
     */
    Result update(PaperDTO paperDTO);

    /**
     * 更新试卷状态
     *
     * @param paperDTO 试卷参数
     * @return Result
     */
    Result setStatus(PaperDTO paperDTO);

    /**
     * 更新试卷考生
     *
     * @param paperDTO 试卷参数
     * @return Result
     */
    Result setUser(PaperDTO paperDTO);

    /**
     * 更新试卷题目
     *
     * @param paperDTO 试卷参数
     * @return Result
     */
    Result setQuestions(PaperDTO paperDTO);

    /**
     * 更新试卷规则
     *
     * @param paperDTO 试卷参数
     * @return Result
     */
    Result setRules(PaperDTO paperDTO);

    /**
     * 试卷归档
     *
     * @param paperDTO 试卷参数
     * @return Result
     */
    Result placeFilePaper(PaperDTO paperDTO);

    /**
     * 批量删除试卷
     *
     * @param ids 主键数组
     * @return Result
     */
    Result delete(Integer[] ids);
}
