package com.njbandou.web.service.impl;

import com.njbandou.web.mapper.MenuMapper;
import com.njbandou.web.repository.MenuRepository;
import com.njbandou.web.service.MenuService;
import com.njbandou.web.vo.PageResult;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public Result getAll() {
        return ResultUtil.success(new PageResult<>(menuMapper.getAll().getResults(), null));
    }
}
