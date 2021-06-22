package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.TitleDTO;
import com.njbandou.web.entity.TitleEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.TitleMapper;
import com.njbandou.web.repository.TitleRepository;
import com.njbandou.web.service.TitleService;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.TitleResult;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class TitleServiceImpl implements TitleService {
    @Autowired
    private TitleRepository titleRepository;
    @Autowired
    private TitleMapper titleMapper;

    @Override
    public Result getAll(Integer[] exceptTitleIds) {
        QueryResults<TitleResult> queryResultDTO = titleMapper.selectAll(exceptTitleIds);
        return ResultUtil.success(queryResultDTO.getResults());
    }

    @Override
    public Result getInviteTitle(Integer[] titleIds) {
        QueryResults<TitleResult> queryResultDTO = titleMapper.selectByIds(titleIds);
        return ResultUtil.success(queryResultDTO.getResults());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(TitleDTO titleDTO) {
        if (titleRepository.findByName(titleDTO.getName()).isPresent()) {
            throw new ServerException("该标签已存在");
        }

        TitleEntity title = titleDTO.insert();
        Optional<TitleEntity> titleEntity = titleRepository.findFirstByDeleteFlagOrderBySortDesc(0);
        if (titleEntity.isPresent()){
            title.setSort(titleEntity.get().getSort()+1);
        }else {
            title.setSort(1);
        }
        titleRepository.save(title);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(TitleDTO titleDTO) {
        TitleEntity title = titleRepository.findById(titleDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        Optional<TitleEntity> otherTitle = titleRepository.findByName(titleDTO.getName());
        if (otherTitle.isPresent() && !otherTitle.get().getPkId().equals(title.getPkId())){
            return ResultUtil.error("该标签已存在");
        }

        titleRepository.saveAndFlush(titleDTO.update(title));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result move(TitleDTO titleDTO) {
        TitleEntity title = titleRepository.findBySort(titleDTO.getSort())
                .orElseThrow(() -> new ServerException("对象不存在"));
        TitleEntity otherTitle = titleDTO.getMove().equals("up") ? titleRepository.findFirstBySortBeforeAndDeleteFlagOrderBySortDesc(titleDTO.getSort(),0)
                .orElseThrow(() -> new ServerException("对象不存在")):
                titleRepository.findFirstBySortAfterAndDeleteFlagOrderBySortAsc(titleDTO.getSort(),0)
                        .orElseThrow(() -> new ServerException("对象不存在"));

        if (titleDTO.getMove().equals("up")){
            title.setSort(otherTitle.getSort());
            otherTitle.setSort(titleDTO.getSort());
        }else {
            title.setSort(otherTitle.getSort());
            otherTitle.setSort(titleDTO.getSort());
        }

        titleRepository.saveAndFlush(title);
        titleRepository.saveAndFlush(otherTitle);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(TitleDTO titleDTO) {
        titleRepository.deleteById(titleDTO.getPkId());
        return ResultUtil.successWithMessage("操作成功");
    }
}
