package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.ExaminationDTO;
import com.njbandou.web.entity.ExaminationEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.repository.ExaminationRepository;
import com.njbandou.web.service.ExaminationService;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.ExaminationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExaminationServiceImpl implements ExaminationService {
    @Autowired
    private ExaminationRepository examinationRepository;

    @Override
    public Result get(Integer paperId) {
        ExaminationEntity examination = examinationRepository.findByPaperId(paperId)
                .orElseThrow(() -> new ServerException("对象不存在"));

        return ResultUtil.success(new ExaminationResult(examination));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(ExaminationDTO examinationDTO) {
        ExaminationEntity examinationEntity = examinationRepository.findById(examinationDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));

        ExaminationEntity examination = examinationDTO.update(examinationEntity);
        examinationRepository.saveAndFlush(examination);

        return ResultUtil.successWithMessage("操作成功",examination);
    }
}
