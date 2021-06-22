package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.ClassDTO;
import com.njbandou.web.dto.search.SearchClassDTO;
import com.njbandou.web.entity.ClassEntity;
import com.njbandou.web.entity.PaperEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.ClassMapper;
import com.njbandou.web.repository.ClassRepository;
import com.njbandou.web.repository.PaperRepository;
import com.njbandou.web.service.ClassService;
import com.njbandou.web.vo.PageResult;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.ClassResult;
import com.querydsl.core.QueryResults;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClassServiceImpl implements ClassService {
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private PaperRepository paperRepository;

    @Override
    public Result getPage(SearchClassDTO searchClassDTO) {
        QueryResults<ClassResult> queryResultDTO = classMapper.selectByPage(searchClassDTO);
        return ResultUtil.success(new PageResult<>(queryResultDTO.getResults(), queryResultDTO.getTotal()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(ClassDTO classDTO) {
        if (classRepository.findByTitle(classDTO.getTitle()).isPresent()) {
            throw new ServerException("该课程已存在");
        }

        classRepository.save(classDTO.insert());

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(ClassDTO classDTO) {
        ClassEntity classEntity = classRepository.findById(classDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        Optional<ClassEntity> otherClass = classRepository.findByTitle(classDTO.getTitle());

        if (otherClass.isPresent() && !classEntity.getPkId().equals(otherClass.get().getPkId())) {
            return ResultUtil.error("该课程已存在！");
        }

        classRepository.saveAndFlush(classDTO.update(classEntity));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result setUser(ClassDTO classDTO) {
        ClassEntity classEntity = classRepository.findById(classDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        if (classDTO.getIsSet()){
            if (StringUtils.isNotBlank(classEntity.getInviteUser()) && StringUtils.isBlank(classDTO.getInviteUser())){
                throw new ServerException("该课程已邀请学员");
            }else if (StringUtils.isNotBlank(classEntity.getInviteTitle()) && StringUtils.isBlank(classDTO.getInviteTitle())){
                throw new ServerException("该课程已邀请标签");
            }else if (StringUtils.isNotBlank(classEntity.getInviteOrganization()) && StringUtils.isBlank(classDTO.getInviteOrganization())){
                throw new ServerException("该课程已邀请组织架构");
            }else if (classEntity.getInviteAll().equals(0) && classDTO.getInviteAll().equals(0)){
                throw new ServerException("该课程已邀请全部");
            }
        }

        ClassEntity newClass = classDTO.setUser(classEntity);
        String paperTitle = "";
        Optional<PaperEntity> paper = paperRepository.findById(newClass.getPaper());
        if (paper.isPresent()){
            paperTitle = paper.get().getTitle();
        }

        return ResultUtil.successWithMessage("操作成功",ClassResult.setResult(newClass,paperTitle));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result setPaper(ClassDTO classDTO) {
        ClassEntity classEntity = classRepository.findById(classDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));

        ClassEntity newClass = classDTO.setClassPaper(classEntity);
        classRepository.saveAndFlush(newClass);
        String paperTitle = "";
        Optional<PaperEntity> paper = paperRepository.findById(newClass.getPaper());
        if (paper.isPresent()){
            paperTitle = paper.get().getTitle();
        }

        return ResultUtil.successWithMessage("操作成功",ClassResult.setResult(newClass,paperTitle));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result setStatus(ClassDTO classDTO) {
        ClassEntity classEntity = classRepository.findById(classDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));

        classEntity.setStatus(classDTO.getStatus());
        classRepository.saveAndFlush(classEntity);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Integer[] ids) {
        for (Integer id : ids) {
           classRepository.deleteById(id);
        }

        return ResultUtil.successWithMessage("操作成功");
    }
}
