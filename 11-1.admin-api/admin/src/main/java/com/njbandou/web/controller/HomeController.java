package com.njbandou.web.controller;

import com.njbandou.web.service.HomeService;
import com.njbandou.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private HomeService homeService;

    @GetMapping(value = "/statistics")
    public Result getAllTitle() {
        return homeService.getStatistics();
    }
}
