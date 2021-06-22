package com.njbandou.web.service;

import com.njbandou.web.dto.modify.KnowledgePointDTO;
import com.njbandou.web.dto.search.SearchKnowledgePointDTO;
import com.njbandou.web.vo.Result;

public interface KnowledgePointService {
    /**
     * 获取知识点列表
     *
     * @param searchKnowledgePointDTO 搜索内容
     * @return PageResult
     */
    Result getPage(SearchKnowledgePointDTO searchKnowledgePointDTO);

    /**
     * 添加知识点
     *
     * @param knowledgePointDTO 知识点参数
     * @return Result
     */
    Result add(KnowledgePointDTO knowledgePointDTO);

    /**
     * 更新知识点信息
     *
     * @param knowledgePointDTO 知识点参数
     * @return Result
     */
    Result update(KnowledgePointDTO knowledgePointDTO);

    /**
     * 批量删除知识点
     *
     * @param ids 主键数组
     * @return Result
     */
    Result delete(Integer[] ids);
}
