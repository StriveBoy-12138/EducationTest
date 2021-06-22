package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.KnowledgePointDTO;
import com.njbandou.web.dto.search.SearchKnowledgePointDTO;
import com.njbandou.web.entity.KnowledgePointEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.KnowledgePointMapper;
import com.njbandou.web.repository.KnowledgePointRepository;
import com.njbandou.web.service.KnowledgePointService;
import com.njbandou.web.vo.PageResult;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.KnowledgePointResult;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class KnowledgePointServiceImpl implements KnowledgePointService {
    @Autowired
    private KnowledgePointRepository knowledgePointRepository;
    @Autowired
    private KnowledgePointMapper knowledgePointMapper;

    @Override
    public Result getPage(SearchKnowledgePointDTO searchKnowledgePointDTO) {
        QueryResults<KnowledgePointResult> queryResultDTO = knowledgePointMapper.selectByPage(searchKnowledgePointDTO);
        return ResultUtil.success(new PageResult<>(queryResultDTO.getResults(), queryResultDTO.getTotal()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(KnowledgePointDTO knowledgePointDTO) {
        if (knowledgePointRepository.findByName(knowledgePointDTO.getName()).isPresent()) {
            throw new ServerException("该知识点已存在");
        }

        knowledgePointRepository.save(knowledgePointDTO.insert());

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(KnowledgePointDTO knowledgePointDTO) {
        KnowledgePointEntity knowledgePointEntity = knowledgePointRepository.findById(knowledgePointDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        Optional<KnowledgePointEntity> otherKnowledgePoint = knowledgePointRepository.findByName(knowledgePointDTO.getName());

        if (otherKnowledgePoint.isPresent() && !knowledgePointEntity.getPkId().equals(otherKnowledgePoint.get().getPkId())) {
            return ResultUtil.error("该知识点已存在！");
        }

        knowledgePointRepository.saveAndFlush(knowledgePointDTO.update(knowledgePointEntity));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Integer[] ids) {
        for (Integer id : ids) {
           knowledgePointRepository.deleteById(id);
        }

        return ResultUtil.successWithMessage("操作成功");
    }
}
