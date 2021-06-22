package com.njbandou.web.service;

import com.njbandou.web.dto.modify.PaperTypeDTO;
import com.njbandou.web.vo.Result;

public interface PaperTypeService {
    /**
     * 获取试卷分类列表
     *
     * @return PageResult
     */
    Result getAll();

    /**
     * 添加试卷分类
     *
     * @param paperTypeDTO 试卷分类参数
     * @return Result
     */
    Result add(PaperTypeDTO paperTypeDTO);

    /**
     * 更新试卷分类信息
     *
     * @param paperTypeDTO 试卷分类参数
     * @return Result
     */
    Result update(PaperTypeDTO paperTypeDTO);

    /**
     * 删除试卷分类
     *
     * @param paperTypeDTO 主键
     * @return Result
     */
    Result delete(PaperTypeDTO paperTypeDTO);
}
