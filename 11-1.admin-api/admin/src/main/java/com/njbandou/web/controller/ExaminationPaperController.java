package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.*;
import com.njbandou.web.dto.search.*;
import com.njbandou.web.service.*;
import com.njbandou.web.vo.*;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/examination/paper")
public class ExaminationPaperController {
    @Autowired
    private ExaminationPaperService Service;

    @GetMapping(value = "/list")
    @RequiresPermissions("paper:manage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paperId", value = "分类id-即分配表的主键pk_id", defaultValue = "0", required = false),
            @ApiImplicitParam(name = "status", value = "考试状态(0:未考试 1:考试中 2:已结束)", defaultValue = "-1", required =
                    false),
            @ApiImplicitParam(name = "isJudge", value = "是否判分(0:是 1:否)", defaultValue = "-1", required = false),
            @ApiImplicitParam(name = "isMark", value = "是否阅卷(0:是 1:否)", defaultValue = "-1", required = false),
            @ApiImplicitParam(name = "startTime", value = "时间段起点", defaultValue = "", required = false),
            @ApiImplicitParam(name = "endTime", value = "时间段终点", defaultValue = "", required = false),
            @ApiImplicitParam(name = "userInfo", value = "用户信息采集(JSONString)", defaultValue = "", required = false),
            @ApiImplicitParam(name = "keyWord", value = "关键词,根据用户nickname筛选", defaultValue = "", required = false),
            @ApiImplicitParam(name = "finishTime", value = "试卷完成时间{up:asc,down:desc}", defaultValue = "", required =
                    false),
            @ApiImplicitParam(name = "scores", value = "用户得分{up:asc,down:desc}", defaultValue = "", required = false),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", defaultValue = "10", required = false),
            @ApiImplicitParam(name = "page", value = "分页页码数", defaultValue = "1", required = false)
    })
    @ApiOperation(value = "根据试卷paperId和查询条件" +
            "{如时间段起点到时间段终点之间等}" +
            "以完成时间或用户得分顺序排序,获取命中条件的试卷数据", notes = "code-{0-成功,1-失败},msg-结果说明," +
            "count-数据条数," +
            "data返回内容:" +
            "<br>pkId,考试试卷表主键" +
            "<br>userId,用户表主键" +
            "<br>userAccount,用户账号" +
            "<br>userName,用户名" +
            "<br>userorganizationId,用户所属组织的主键" +
            "<br>organizationName,用户所属组织名" +
            "<br>userInfo,用户信息" +
            "<br>paperId,试卷表对应主键" +
            "<br>questions,试卷题目" +
            "<br>userScore,用户得分" +
            "<br>finishTime,完成时间" +
            "<br>useTime,考试用时" +
            "<br>isJudge,是否判分(0:是 1:否)" +
            "<br>isMark,是否阅卷(0:是 1:否)" +
            "<br>status考试状态(0:未考试 1:考试中 2:已结束),")
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
        return Service.getPage(searchExaminationPaperDTO);
    }

    @PostMapping(value = "/mark")
    @RequiresPermissions("paper:manage")
    @ApiOperation(value = "根据考试试卷pkId,修改为已阅卷状态", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数")
    public Result setStatus(@RequestBody @ApiParam(name = "DTO", value = "考试试卷表dto") ExaminationPaperDTO DTO) {
        return Service.mark(DTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("paper:manage")
    @ApiOperation(value = "根据考试试卷pkId,批量删除数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    @ApiImplicitParam(name = "Ids", value = "int数组-考试试卷pkId")
    public Result delete(@RequestBody Integer[] Ids) {
        return Service.delete(Ids);
    }
}
