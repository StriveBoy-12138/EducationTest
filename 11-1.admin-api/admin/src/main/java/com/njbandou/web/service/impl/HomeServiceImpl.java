package com.njbandou.web.service.impl;

import com.njbandou.web.repository.*;
import com.njbandou.web.service.HomeService;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.StatisticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private CoursewareRepository coursewareRepository;

    @Override
    public Result getStatistics() {
        Integer paperNum = paperRepository.countDistinctByDeleteFlag(0);
        Integer questionNum = questionRepository.countDistinctByDeleteFlag(0);
        Integer userNum = userRepository.countDistinctByDeleteFlag(0);
        Integer classNum = classRepository.countDistinctByDeleteFlag(0);
        Integer coursewareNum = coursewareRepository.countDistinctByDeleteFlag(0);
        StatisticsResult statisticsResult = new StatisticsResult(paperNum, questionNum, userNum, classNum, coursewareNum);
        return ResultUtil.success(statisticsResult);
    }
}
