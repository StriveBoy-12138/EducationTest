package com.njbandou.web.service;

import com.njbandou.web.dto.modify.ClassTypeDTO;
import com.njbandou.web.vo.Result;

public interface ClassTypeService {
    /**
     * 获取课程分类列表
     *
     * @return PageResult
     */
    Result getAll();

    /**
     * 添加课程分类
     *
     * @param classTypeDTO 课程分类参数
     * @return Result
     */
    Result add(ClassTypeDTO classTypeDTO);

    /**
     * 更新课程分类信息
     *
     * @param classTypeDTO 课程分类参数
     * @return Result
     */
    Result update(ClassTypeDTO classTypeDTO);

    /**
     * 删除课程分类
     *
     * @param classTypeDTO 主键
     * @return Result
     */
    Result delete(ClassTypeDTO classTypeDTO);
}
