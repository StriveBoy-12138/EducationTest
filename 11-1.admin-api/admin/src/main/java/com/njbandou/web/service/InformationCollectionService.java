package com.njbandou.web.service;

import com.njbandou.web.dto.modify.InformationCollectionDTO;
import com.njbandou.web.dto.search.SearchInformationCollectionDTO;
import com.njbandou.web.vo.Result;

public interface InformationCollectionService {
    /**
     * 获取所有信息采集项
     * @return
     */
    Result getAll(SearchInformationCollectionDTO searchInformationCollectionDTO);

    /**
     * 添加信息采集项
     * @param informationCollectionDTO
     * @return
     */
    Result add(InformationCollectionDTO informationCollectionDTO);

    /**
     * 修改信息采集项
     * @param informationCollectionDTO
     * @return
     */
    Result update(InformationCollectionDTO informationCollectionDTO);

    /**
     * 下移上移信息采集项
     * @param informationCollectionDTO
     * @return
     */
    Result move(InformationCollectionDTO informationCollectionDTO);

    /**
     * 删除信息采集项
     * @param informationCollectionDTO
     * @return
     */
    Result delete(InformationCollectionDTO informationCollectionDTO);
}
