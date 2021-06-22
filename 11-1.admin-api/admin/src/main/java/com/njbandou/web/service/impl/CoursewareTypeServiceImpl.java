package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.CoursewareTypeDTO;
import com.njbandou.web.entity.CoursewareTypeEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.CoursewareTypeMapper;
import com.njbandou.web.repository.CoursewareTypeRepository;
import com.njbandou.web.service.CoursewareTypeService;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.CoursewareTypeResult;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class CoursewareTypeServiceImpl implements CoursewareTypeService {
    @Autowired
    private CoursewareTypeRepository coursewareTypeRepository;
    @Autowired
    private CoursewareTypeMapper coursewareTypeMapper;

    @Override
    public Result getAll() {
        QueryResults<CoursewareTypeResult> queryResultDTO = coursewareTypeMapper.selectAll();
        return ResultUtil.success(queryResultDTO.getResults());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(CoursewareTypeDTO coursewareTypeDTO) {
        if (coursewareTypeRepository.findByName(coursewareTypeDTO.getName()).isPresent()) {
            throw new ServerException("该分类已存在");
        }

        CoursewareTypeEntity coursewareType = coursewareTypeDTO.insert();
        coursewareTypeRepository.save(coursewareType);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(CoursewareTypeDTO coursewareTypeDTO) {
        CoursewareTypeEntity coursewareType = coursewareTypeRepository.findById(coursewareTypeDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        Optional<CoursewareTypeEntity> otherCoursewareType = coursewareTypeRepository.findByName(coursewareTypeDTO.getName());
        if (otherCoursewareType.isPresent() && !otherCoursewareType.get().getPkId().equals(coursewareType.getPkId())){
            return ResultUtil.error("该分类已存在");
        }

        coursewareTypeRepository.saveAndFlush(coursewareTypeDTO.update(coursewareType));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(CoursewareTypeDTO coursewareTypeDTO) {
        coursewareTypeRepository.deleteById(coursewareTypeDTO.getPkId());
        return ResultUtil.successWithMessage("操作成功");
    }
}
