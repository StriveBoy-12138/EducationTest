package com.njbandou.web.service;

import com.njbandou.web.dto.modify.ClassDTO;
import com.njbandou.web.dto.search.SearchClassDTO;
import com.njbandou.web.vo.Result;

public interface ClassService {
    /**
     * 获取课程列表
     *
     * @param searchClassDTO 搜索内容
     * @return PageResult
     */
    Result getPage(SearchClassDTO searchClassDTO);

    /**
     * 添加课程
     *
     * @param classDTO 课程参数
     * @return Result
     */
    Result add(ClassDTO classDTO);

    /**
     * 更新课程信息
     *
     * @param classDTO 课程参数
     * @return Result
     */
    Result update(ClassDTO classDTO);

    /**
     * 更新课程信息
     *
     * @param classDTO 课程参数
     * @return Result
     */
    Result setUser(ClassDTO classDTO);

    /**
     * 更新课程信息
     *
     * @param classDTO 课程参数
     * @return Result
     */
    Result setPaper(ClassDTO classDTO);

    /**
     * 更新课程状态
     *
     * @param classDTO 课程参数
     * @return Result
     */
    Result setStatus(ClassDTO classDTO);

    /**
     * 批量删除课程
     *
     * @param ids 主键数组
     * @return Result
     */
    Result delete(Integer[] ids);
}
