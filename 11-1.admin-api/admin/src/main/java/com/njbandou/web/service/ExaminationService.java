package com.njbandou.web.service;

import com.njbandou.web.dto.modify.ExaminationDTO;
import com.njbandou.web.vo.Result;

public interface ExaminationService {
    /**
     * 获取考试设置
     *
     * @param paperId
     * @return PageResult
     */
    Result get(Integer paperId);

    /**
     * 更新考试设置
     *
     * @param examinationDTO 试卷参数
     * @return Result
     */
    Result update(ExaminationDTO examinationDTO);
}
