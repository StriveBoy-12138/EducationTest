package com.njbandou.web.service;

import com.njbandou.web.dto.modify.CatalogCoursewareDTO;
import com.njbandou.web.dto.search.SearchCatalogCoursewareDTO;
import com.njbandou.web.vo.Result;

import java.util.List;

public interface CatalogCoursewareService {
    /**
     * 获取目录课件列表
     *
     * @return PageResult
     */
    Result getAll(SearchCatalogCoursewareDTO searchCatalogCoursewareDTO);

    /**
     * 添加目录课件
     *
     * @param catalogCoursewareList 目录参数
     * @return Result
     */
    Result add(List<CatalogCoursewareDTO> catalogCoursewareList);

    /**
     * 下移上移目录课件
     * @param catalogCoursewareDTO
     * @return
     */
    Result move(CatalogCoursewareDTO catalogCoursewareDTO);

    /**
     * 删除目录课件
     *
     * @param catalogCoursewareDTO 主键
     * @return Result
     */
    Result delete(CatalogCoursewareDTO catalogCoursewareDTO);
}
