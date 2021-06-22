package com.njbandou.web.service;

import com.njbandou.web.dto.modify.CoursewareDTO;
import com.njbandou.web.dto.search.SearchCoursewareDTO;
import com.njbandou.web.vo.Result;

public interface CoursewareService {
    /**
     * 获取课件列表
     *
     * @param searchCoursewareDTO 搜索内容
     * @return PageResult
     */
    Result getPage(SearchCoursewareDTO searchCoursewareDTO);

    /**
     * 添加课件
     *
     * @param coursewareDTO 课件参数
     * @return Result
     */
    Result add(CoursewareDTO coursewareDTO);

    /**
     * 更新课件信息
     *
     * @param coursewareDTO 课件参数
     * @return Result
     */
    Result update(CoursewareDTO coursewareDTO);

    /**
     * 批量删除课件
     *
     * @param ids 主键数组
     * @return Result
     */
    Result delete(Integer[] ids);
}
