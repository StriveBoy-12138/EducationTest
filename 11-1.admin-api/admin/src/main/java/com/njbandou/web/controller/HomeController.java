package com.njbandou.web.controller;

import com.njbandou.web.service.HomeService;
import com.njbandou.web.vo.Result;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private HomeService homeService;

    @GetMapping(value = "/statistics")
    @ApiOperation(value = "获取统计数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回内容:"+
            "<br>paperNum,试卷总数" +
            "<br>questionNum,题目总数" +
            "<br>userNum,用户总数" +
            "<br>classNum,课程总数" +
            "<br>coursewareNum,课件总数")
    public Result getAllTitle() {
        return homeService.getStatistics();
    }
}
