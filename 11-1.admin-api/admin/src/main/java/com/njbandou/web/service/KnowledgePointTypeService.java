package com.njbandou.web.service;

import com.njbandou.web.dto.modify.KnowledgePointTypeDTO;
import com.njbandou.web.vo.Result;

public interface KnowledgePointTypeService {
    /**
     * 获取知识点分类分类列表
     *
     * @return PageResult
     */
    Result getAll();

    /**
     * 添加知识点分类分类
     *
     * @param knowledgePointType 知识点分类分类参数
     * @return Result
     */
    Result add(KnowledgePointTypeDTO knowledgePointType);

    /**
     * 更新知识点分类分类信息
     *
     * @param knowledgePointType 知识点分类分类参数
     * @return Result
     */
    Result update(KnowledgePointTypeDTO knowledgePointType);

    /**
     * 删除知识点分类分类
     *
     * @param knowledgePointType 主键
     * @return Result
     */
    Result delete(KnowledgePointTypeDTO knowledgePointType);
}
