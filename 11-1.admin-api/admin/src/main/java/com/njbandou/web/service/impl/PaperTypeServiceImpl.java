package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.PaperTypeDTO;
import com.njbandou.web.entity.PaperTypeEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.PaperTypeMapper;
import com.njbandou.web.repository.PaperTypeRepository;
import com.njbandou.web.service.PaperTypeService;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.PaperTypeResult;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class PaperTypeServiceImpl implements PaperTypeService {
    @Autowired
    private PaperTypeRepository paperTypeRepository;
    @Autowired
    private PaperTypeMapper paperTypeMapper;

    @Override
    public Result getAll() {
        QueryResults<PaperTypeResult> queryResultDTO = paperTypeMapper.selectAll();
        return ResultUtil.success(queryResultDTO.getResults());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(PaperTypeDTO paperTypeDTO) {
        if (paperTypeRepository.findByName(paperTypeDTO.getName()).isPresent()) {
            throw new ServerException("该分类已存在");
        }

        PaperTypeEntity paperType = paperTypeDTO.insert();
        paperTypeRepository.save(paperType);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(PaperTypeDTO paperTypeDTO) {
        PaperTypeEntity paperType = paperTypeRepository.findById(paperTypeDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        Optional<PaperTypeEntity> otherPaperType = paperTypeRepository.findByName(paperTypeDTO.getName());
        if (otherPaperType.isPresent() && !otherPaperType.get().getPkId().equals(paperType.getPkId())){
            return ResultUtil.error("该分类已存在");
        }

        paperTypeRepository.saveAndFlush(paperTypeDTO.update(paperType));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(PaperTypeDTO paperTypeDTO) {
        paperTypeRepository.deleteById(paperTypeDTO.getPkId());
        return ResultUtil.successWithMessage("操作成功");
    }
}
