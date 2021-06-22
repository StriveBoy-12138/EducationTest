package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.QuestionBankTypeDTO;
import com.njbandou.web.entity.QuestionBankTypeEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.QuestionBankTypeMapper;
import com.njbandou.web.repository.QuestionBankTypeRepository;
import com.njbandou.web.service.QuestionBankTypeService;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.QuestionBankTypeResult;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class QuestionBankTypeServiceImpl implements QuestionBankTypeService {
    @Autowired
    private QuestionBankTypeRepository questionBankTypeRepository;
    @Autowired
    private QuestionBankTypeMapper questionBankTypeMapper;

    @Override
    public Result getAll() {
        QueryResults<QuestionBankTypeResult> queryResultDTO = questionBankTypeMapper.selectAll();
        return ResultUtil.success(queryResultDTO.getResults());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(QuestionBankTypeDTO questionBankTypeDTO) {
        if (questionBankTypeRepository.findByName(questionBankTypeDTO.getName()).isPresent()) {
            throw new ServerException("该分类已存在");
        }

        QuestionBankTypeEntity questionBankType = questionBankTypeDTO.insert();
        questionBankTypeRepository.save(questionBankType);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(QuestionBankTypeDTO questionBankTypeDTO) {
        QuestionBankTypeEntity questionBankType = questionBankTypeRepository.findById(questionBankTypeDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        Optional<QuestionBankTypeEntity> otherQuestionBankType = questionBankTypeRepository.findByName(questionBankTypeDTO.getName());
        if (otherQuestionBankType.isPresent() && !otherQuestionBankType.get().getPkId().equals(questionBankType.getPkId())){
            return ResultUtil.error("该分类已存在");
        }

        questionBankTypeRepository.saveAndFlush(questionBankTypeDTO.update(questionBankType));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(QuestionBankTypeDTO questionBankTypeDTO) {
        questionBankTypeRepository.deleteById(questionBankTypeDTO.getPkId());
        return ResultUtil.successWithMessage("操作成功");
    }
}
