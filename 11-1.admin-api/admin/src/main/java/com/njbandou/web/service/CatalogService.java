package com.njbandou.web.service;

import com.njbandou.web.dto.modify.CatalogDTO;
import com.njbandou.web.dto.search.SearchCatalogDTO;
import com.njbandou.web.vo.Result;

public interface CatalogService {
    /**
     * 获取目录列表
     *
     * @return PageResult
     */
    Result getAll(SearchCatalogDTO searchCatalogDTO);

    /**
     * 添加目录
     *
     * @param catalogDTO 目录参数
     * @return Result
     */
    Result add(CatalogDTO catalogDTO);

    /**
     * 更新目录信息
     *
     * @param catalogDTO 目录参数
     * @return Result
     */
    Result update(CatalogDTO catalogDTO);

    /**
     * 下移上移目录
     * @param catalogDTO
     * @return
     */
    Result move(CatalogDTO catalogDTO);

    /**
     * 删除目录
     *
     * @param catalogDTO 主键
     * @return Result
     */
    Result delete(CatalogDTO catalogDTO);
}
