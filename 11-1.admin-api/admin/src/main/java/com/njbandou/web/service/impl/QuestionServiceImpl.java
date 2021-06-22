package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.QuestionDTO;
import com.njbandou.web.dto.search.SearchQuestionDTO;
import com.njbandou.web.entity.QuestionEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.QuestionMapper;
import com.njbandou.web.repository.KnowledgePointRepository;
import com.njbandou.web.repository.QuestionRepository;
import com.njbandou.web.service.QuestionService;
import com.njbandou.web.utils.EnumUtils;
import com.njbandou.web.vo.PageResult;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.QuestionNumberResult;
import com.njbandou.web.vo.item.QuestionResult;
import com.querydsl.core.QueryResults;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private KnowledgePointRepository knowledgePointRepository;

    @Override
    public Result getPage(SearchQuestionDTO searchQuestionDTO) {
        QueryResults<QuestionResult> queryResult = questionMapper.selectByPage(searchQuestionDTO);
        queryResult.getResults().forEach(question -> {
            String[] knowledgePoint = question.getKnowledgePoint().split(",");
            Integer[] ids = new Integer[knowledgePoint.length];
            for (int i = 0;i < knowledgePoint.length;i ++) {
                if (StringUtils.isNotBlank(knowledgePoint[i])){
                    ids[i] = Integer.valueOf(knowledgePoint[i]);
                }
            }
            question.setQuestionKnowledgePoint(knowledgePointRepository.findByIds(ids));
            question.setTypeName(EnumUtils.getQuestionType(question.getType()));
            if (StringUtils.isNotBlank(question.getFileName())){
                question.setFileNameList(question.getFileName().split(","));
            }
            if (StringUtils.isNotBlank(question.getFile())){
                question.setFileList(question.getFile().split(","));
            }
        });
        return ResultUtil.success(new PageResult<>(queryResult.getResults(), queryResult.getTotal()));
    }

    @Override
    public Result getRandomQuestions(SearchQuestionDTO searchQuestionDTO) {
        List<QuestionResult> questionResults  = new ArrayList<>();
        searchQuestionDTO.getRules().forEach(rule -> {
            QueryResults<QuestionResult> queryResult = questionMapper.selectByRules(searchQuestionDTO.getRuleId(),rule);
            queryResult.getResults().forEach(questionResult -> {
                questionResult.setScore(rule.getScore());
            });
            questionResults.addAll(queryResult.getResults());
        });

        return ResultUtil.success(questionResults);
    }

    @Override
    public Result getNumber() {
        Integer[] type = new Integer[5];
        for (int i = 0;i < 5;i ++){
            Integer number = questionRepository.countDistinctByType(i);
            type[i] = number;
        }
        Integer[] difficulty = new Integer[3];
        for (int i = 0;i < 3;i ++){
            Integer number = questionRepository.countDistinctByDifficulty(i);
            difficulty[i] = number;
        }

        return ResultUtil.success(new QuestionNumberResult(type,difficulty));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(QuestionDTO questionDTO) {
        if (questionRepository.findByTitleAndBankId(questionDTO.getTitle(),questionDTO.getBankId()).isPresent()) {
            throw new ServerException("该题干已存在");
        }

        questionRepository.save(questionDTO.insert());
        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(QuestionDTO questionDTO) {
        QuestionEntity questionEntity = questionRepository.findById(questionDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        Optional<QuestionEntity> otherQuestion = questionRepository.findByTitleAndBankId(questionDTO.getTitle(),questionDTO.getBankId());

        if (otherQuestion.isPresent() && !questionEntity.getPkId().equals(otherQuestion.get().getPkId())) {
            return ResultUtil.error("该题干已存在！");
        }
        questionRepository.saveAndFlush(questionDTO.update(questionEntity));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result setKnowledgePoint(QuestionDTO questionDTO) {
        QuestionEntity questionEntity = questionRepository.findById(questionDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));

        QuestionEntity question = questionDTO.setQuestionKnowledgePoint(questionEntity);
        questionRepository.saveAndFlush(question);

        return ResultUtil.successWithMessage("操作成功",question);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Integer[] ids) {
        for (Integer id : ids) {
           questionRepository.deleteById(id);
        }

        return ResultUtil.successWithMessage("操作成功");
    }
}
