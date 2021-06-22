package com.njbandou.web.service;

import com.njbandou.web.dto.modify.CoursewareTypeDTO;
import com.njbandou.web.vo.Result;

public interface CoursewareTypeService {
    /**
     * 获取课件分类列表
     *
     * @return PageResult
     */
    Result getAll();

    /**
     * 添加课件分类
     *
     * @param coursewareType 课件分类参数
     * @return Result
     */
    Result add(CoursewareTypeDTO coursewareType);

    /**
     * 更新课件分类信息
     *
     * @param coursewareType 课件分类参数
     * @return Result
     */
    Result update(CoursewareTypeDTO coursewareType);

    /**
     * 删除课件分类
     *
     * @param coursewareType 主键
     * @return Result
     */
    Result delete(CoursewareTypeDTO coursewareType);
}
