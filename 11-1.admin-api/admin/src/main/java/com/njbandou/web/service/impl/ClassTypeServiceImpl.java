package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.ClassTypeDTO;
import com.njbandou.web.entity.ClassTypeEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.ClassTypeMapper;
import com.njbandou.web.repository.ClassTypeRepository;
import com.njbandou.web.service.ClassTypeService;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.ClassTypeResult;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class ClassTypeServiceImpl implements ClassTypeService {
    @Autowired
    private ClassTypeRepository classTypeRepository;
    @Autowired
    private ClassTypeMapper classTypeMapper;

    @Override
    public Result getAll() {
        QueryResults<ClassTypeResult> queryResultDTO = classTypeMapper.selectAll();
        return ResultUtil.success(queryResultDTO.getResults());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(ClassTypeDTO classTypeDTO) {
        if (classTypeRepository.findByName(classTypeDTO.getName()).isPresent()) {
            throw new ServerException("该分类已存在");
        }

        ClassTypeEntity classType = classTypeDTO.insert();
        classTypeRepository.save(classType);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(ClassTypeDTO classTypeDTO) {
        ClassTypeEntity classType = classTypeRepository.findById(classTypeDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        Optional<ClassTypeEntity> otherClassType = classTypeRepository.findByName(classTypeDTO.getName());
        if (otherClassType.isPresent() && !otherClassType.get().getPkId().equals(classType.getPkId())){
            return ResultUtil.error("该分类已存在");
        }

        classTypeRepository.saveAndFlush(classTypeDTO.update(classType));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(ClassTypeDTO classTypeDTO) {
        classTypeRepository.deleteById(classTypeDTO.getPkId());
        return ResultUtil.successWithMessage("操作成功");
    }
}
