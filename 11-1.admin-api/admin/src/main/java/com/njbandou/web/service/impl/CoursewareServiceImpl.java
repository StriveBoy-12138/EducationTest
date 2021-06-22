package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.CoursewareDTO;
import com.njbandou.web.dto.search.SearchCoursewareDTO;
import com.njbandou.web.entity.CoursewareEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.CoursewareMapper;
import com.njbandou.web.repository.CoursewareRepository;
import com.njbandou.web.service.CoursewareService;
import com.njbandou.web.vo.PageResult;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.CoursewareResult;
import com.querydsl.core.QueryResults;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CoursewareServiceImpl implements CoursewareService {
    @Autowired
    private CoursewareRepository coursewareRepository;
    @Autowired
    private CoursewareMapper coursewareMapper;

    @Override
    public Result getPage(SearchCoursewareDTO searchCoursewareDTO) {
        QueryResults<CoursewareResult> queryResultDTO = coursewareMapper.selectByPage(searchCoursewareDTO);
        return ResultUtil.success(new PageResult<>(queryResultDTO.getResults(), queryResultDTO.getTotal()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(CoursewareDTO coursewareDTO) {
        if (coursewareRepository.findByTitle(coursewareDTO.getTitle()).isPresent()) {
            throw new ServerException("该课件已存在");
        }

        coursewareRepository.save(coursewareDTO.insert());

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(CoursewareDTO coursewareDTO) {
        CoursewareEntity coursewareEntity = coursewareRepository.findById(coursewareDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        Optional<CoursewareEntity> otherCourseware = coursewareRepository.findByTitle(coursewareDTO.getTitle());

        if (otherCourseware.isPresent() && !coursewareEntity.getPkId().equals(otherCourseware.get().getPkId())) {
            return ResultUtil.error("该课件已存在！");
        }

        coursewareRepository.saveAndFlush(coursewareDTO.update(coursewareEntity));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Integer[] ids) {
        for (Integer id : ids) {
           coursewareRepository.deleteById(id);
        }

        return ResultUtil.successWithMessage("操作成功");
    }
}
