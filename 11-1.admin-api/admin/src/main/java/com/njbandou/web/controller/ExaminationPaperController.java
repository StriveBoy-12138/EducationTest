package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.ExaminationPaperDTO;
import com.njbandou.web.dto.search.SearchExaminationPaperDTO;
import com.njbandou.web.service.ExaminationPaperService;
import com.njbandou.web.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/examination/paper")
public class ExaminationPaperController {
    @Autowired
    private ExaminationPaperService examinationPaperService;

    @GetMapping(value = "/list")
    @RequiresPermissions("paper:manage")
    public Result list(@RequestParam(required = false, defaultValue = "0") Integer paperId,
                       @RequestParam(required = false, defaultValue = "-1") Integer status,
                       @RequestParam(required = false, defaultValue = "-1") Integer isJudge,
                       @RequestParam(required = false, defaultValue = "-1") Integer isMark,
                       @RequestParam(required = false, defaultValue = "") String startTime,
                       @RequestParam(required = false, defaultValue = "") String endTime,
                       @RequestParam(required = false, defaultValue = "") String userInfo,
                       @RequestParam(required = false, defaultValue = "") String keyWord,
                       @RequestParam(required = false, defaultValue = "") String finishTime,
                       @RequestParam(required = false, defaultValue = "") String scores,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false, defaultValue = "1") Integer page) {
        SearchExaminationPaperDTO searchExaminationPaperDTO = new SearchExaminationPaperDTO();
        searchExaminationPaperDTO.setStatus(status);
        searchExaminationPaperDTO.setPaperId(paperId);
        searchExaminationPaperDTO.setIsJudge(isJudge);
        searchExaminationPaperDTO.setIsMark(isMark);
        searchExaminationPaperDTO.setStartTime(startTime);
        searchExaminationPaperDTO.setEndTime(endTime);
        searchExaminationPaperDTO.setUserInfo(userInfo);
        searchExaminationPaperDTO.setKeyWord(keyWord);
        searchExaminationPaperDTO.setFinishTime(finishTime);
        searchExaminationPaperDTO.setScores(scores);
        searchExaminationPaperDTO.setPage(page);
        searchExaminationPaperDTO.setPageSize(pageSize);
        return examinationPaperService.getPage(searchExaminationPaperDTO);
    }

    @PostMapping(value = "/mark")
    @RequiresPermissions("paper:manage")
    public Result setStatus(@RequestBody ExaminationPaperDTO examinationPaperDTO) {
        return examinationPaperService.mark(examinationPaperDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("paper:manage")
    public Result delete(@RequestBody Integer[] examinationPaperIds) {
        return examinationPaperService.delete(examinationPaperIds);
    }
}
