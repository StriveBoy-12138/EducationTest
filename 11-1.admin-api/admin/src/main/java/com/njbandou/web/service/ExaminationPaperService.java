package com.njbandou.web.service;

import com.njbandou.web.dto.modify.ExaminationPaperDTO;
import com.njbandou.web.dto.search.SearchExaminationPaperDTO;
import com.njbandou.web.vo.Result;

public interface ExaminationPaperService {
    /**
     * 获取考生列表
     *
     * @param searchExaminationPaperDTO 搜索内容
     * @return PageResult
     */
    Result getPage(SearchExaminationPaperDTO searchExaminationPaperDTO);

    /**
     * 阅卷
     *
     * @param examinationPaperDTO
     * @return PageResult
     */
    Result mark(ExaminationPaperDTO examinationPaperDTO);

    /**
     * 删除
     *
     * @param ids
     * @return PageResult
     */
    Result delete(Integer[] ids);
}
