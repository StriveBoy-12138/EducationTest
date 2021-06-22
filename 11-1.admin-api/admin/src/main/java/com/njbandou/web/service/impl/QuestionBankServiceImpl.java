package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.QuestionBankDTO;
import com.njbandou.web.dto.search.SearchQuestionBankDTO;
import com.njbandou.web.entity.QuestionBankEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.QuestionBankMapper;
import com.njbandou.web.repository.QuestionBankRepository;
import com.njbandou.web.repository.QuestionRepository;
import com.njbandou.web.service.QuestionBankService;
import com.njbandou.web.vo.PageResult;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.QuestionBankResult;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {
    @Autowired
    private QuestionBankRepository questionBankRepository;
    @Autowired
    private QuestionBankMapper questionBankMapper;
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Result getPage(SearchQuestionBankDTO searchQuestionBankDTO) {
        QueryResults<QuestionBankResult> queryResultDTO = questionBankMapper.selectByPage(searchQuestionBankDTO);
        queryResultDTO.getResults().forEach(questionBank -> {
            questionBank.setQuestionNum(questionRepository.countDistinctByBankId(questionBank.getPkId()));
        });
        return ResultUtil.success(new PageResult<>(queryResultDTO.getResults(), queryResultDTO.getTotal()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(QuestionBankDTO questionBankDTO) {
        if (questionBankRepository.findByName(questionBankDTO.getName()).isPresent()) {
            throw new ServerException("该题库已存在");
        }

        questionBankRepository.save(questionBankDTO.insert());

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(QuestionBankDTO questionBankDTO) {
        QuestionBankEntity questionBankEntity = questionBankRepository.findById(questionBankDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        Optional<QuestionBankEntity> otherQuestionBank = questionBankRepository.findByName(questionBankDTO.getName());

        if (otherQuestionBank.isPresent() && !questionBankEntity.getPkId().equals(otherQuestionBank.get().getPkId())) {
            return ResultUtil.error("该题库已存在！");
        }

        questionBankRepository.saveAndFlush(questionBankDTO.update(questionBankEntity));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Integer[] ids) {
        for (Integer id : ids) {
           questionBankRepository.deleteById(id);
        }

        return ResultUtil.successWithMessage("操作成功");
    }
}
