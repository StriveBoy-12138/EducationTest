package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.KnowledgePointTypeDTO;
import com.njbandou.web.entity.KnowledgePointTypeEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.KnowledgePointTypeMapper;
import com.njbandou.web.repository.KnowledgePointTypeRepository;
import com.njbandou.web.service.KnowledgePointTypeService;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.KnowledgePointTypeResult;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class KnowledgePointTypeServiceImpl implements KnowledgePointTypeService {
    @Autowired
    private KnowledgePointTypeRepository knowledgePointTypeRepository;
    @Autowired
    private KnowledgePointTypeMapper knowledgePointTypeMapper;

    @Override
    public Result getAll() {
        QueryResults<KnowledgePointTypeResult> queryResultDTO = knowledgePointTypeMapper.selectAll();
        return ResultUtil.success(queryResultDTO.getResults());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(KnowledgePointTypeDTO knowledgePointTypeDTO) {
        if (knowledgePointTypeRepository.findByName(knowledgePointTypeDTO.getName()).isPresent()) {
            throw new ServerException("该分类已存在");
        }

        KnowledgePointTypeEntity knowledgePointType = knowledgePointTypeDTO.insert();
        knowledgePointTypeRepository.save(knowledgePointType);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(KnowledgePointTypeDTO knowledgePointTypeDTO) {
        KnowledgePointTypeEntity knowledgePointType = knowledgePointTypeRepository.findById(knowledgePointTypeDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        Optional<KnowledgePointTypeEntity> otherKnowledgePointType = knowledgePointTypeRepository.findByName(knowledgePointTypeDTO.getName());
        if (otherKnowledgePointType.isPresent() && !otherKnowledgePointType.get().getPkId().equals(knowledgePointType.getPkId())){
            return ResultUtil.error("该分类已存在");
        }

        knowledgePointTypeRepository.saveAndFlush(knowledgePointTypeDTO.update(knowledgePointType));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(KnowledgePointTypeDTO knowledgePointTypeDTO) {
        knowledgePointTypeRepository.deleteById(knowledgePointTypeDTO.getPkId());
        return ResultUtil.successWithMessage("操作成功");
    }
}
