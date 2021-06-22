package com.njbandou.web.service;

import com.njbandou.web.dto.modify.TitleDTO;
import com.njbandou.web.vo.Result;

public interface TitleService {
    /**
     * 获取标签列表
     */
    Result getAll(Integer[] exceptTitleIds);

    /**
     * 获取邀请标签列表
     */
    Result getInviteTitle(Integer[] titleIds);

    /**
     * 添加标签
     *
     * @param titleDTO 标签参数
     * @return Result
     */
    Result add(TitleDTO titleDTO);

    /**
     * 更新标签信息
     *
     * @param titleDTO 标签参数
     * @return Result
     */
    Result update(TitleDTO titleDTO);

    /**
     * 下移上移标签
     * @param titleDTO
     * @return
     */
    Result move(TitleDTO titleDTO);

    /**
     * 删除标签
     *
     * @param titleDTO 主键
     * @return Result
     */
    Result delete(TitleDTO titleDTO);
}
