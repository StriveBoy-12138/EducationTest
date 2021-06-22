package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.ExaminationPaperDTO;
import com.njbandou.web.dto.search.SearchExaminationPaperDTO;
import com.njbandou.web.entity.ExaminationPaperEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.ExaminationPaperMapper;
import com.njbandou.web.repository.ExaminationPaperRepository;
import com.njbandou.web.repository.PaperRepository;
import com.njbandou.web.service.ExaminationPaperService;
import com.njbandou.web.vo.PageResult;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.ExaminationPaperResult;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExaminationPaperServiceImpl implements ExaminationPaperService {
    @Autowired
    private ExaminationPaperRepository examinationPaperRepository;
    @Autowired
    private ExaminationPaperMapper examinationPaperMapper;

    @Override
    public Result getPage(SearchExaminationPaperDTO searchExaminationPaperDTO) {
        QueryResults<ExaminationPaperResult> queryResultDTO = examinationPaperMapper.selectByPage(searchExaminationPaperDTO);
        return ResultUtil.success(new PageResult<>(queryResultDTO.getResults(), queryResultDTO.getTotal()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result mark(ExaminationPaperDTO examinationPaperDTO) {
        ExaminationPaperEntity examinationPaper = examinationPaperRepository.findById(examinationPaperDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));

        examinationPaperRepository.saveAndFlush(examinationPaperDTO.mark(examinationPaper));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Integer[] ids) {
        for (Integer id : ids) {
            examinationPaperRepository.deleteById(id);
        }

        return ResultUtil.successWithMessage("操作成功");
    }
}
