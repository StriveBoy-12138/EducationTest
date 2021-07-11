package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.*;
import com.njbandou.web.service.*;
import com.njbandou.web.vo.*;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/examination")
public class ExaminationController {
    @Autowired
    private ExaminationService examinationService;

    @GetMapping(value = "/get")
    @RequiresPermissions("paper:set")
    @ApiOperation(value = "根据试卷paperId,获取对应id的一条考试设置数据", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回内容:"
            + "<br>pkId-int(11)-主键"
            + "<br>paperId-int(11)-试卷id"
            + "<br>time-int(11)-时长(分钟)"
            + "<br>questionLimit-int(11)-按题限时(0:是 1:否)"
            + "<br>passingGrade-int(11)-及格分"
            + "<br>brakBarrier-int(11)-闯关模式(0:是 1:否)"
            + "<br>examinationTimeUse-int(11)-启用考试时间(0:是 1:否)"
            + "<br>考试时间(JSONString)(\"" +
            "{\"startTime\":\"2021-3-8 12:00\"," +
            "\"endTime\":\"2021-3-8 13:00\"," +
            "\"viewResult\":1," +
            "\"isLate\":0," +
            "\"lateTime\":10," +
            "\"isSubmit\":0," +
            "\"submitTime\":10}\")"
            + "<br>multipleExamination-int(11)-多次考试(0:是 1:否)"
            + "<br>multipleExamination_time-int(11)-多次考试次数"
            + "<br>chargeExamination-int(11)-收费考试(0:是 1:否)"
            + "<br>chargeExamination_price-decimal(10,2)-收费考试价格"
            + "<br>sign-int(11)-是否需要签名(0:是 1:否)"
            + "<br>scoreDisplay-int(11)-是否显示分值(0:是 1:否)"
            + "<br>questionTypeDisplay-int(11)-是否显示题型(0:是 1:否)"
            + "<br>randomOrder-varchar(16)-随机顺序(0:题目 1:选项)多选时,隔开"
            + "<br>handAfterFinish-int(11)-全部答完才能交卷(0:是 1:否)"
            + "<br>skipQuestion-int(11)-单选和判断自动跳题(0:是 1:否)"
            + "<br>preventionCheat-int(11)-防作弊(0:是 1:否)"
            + "<br>preventionCheat_time-int(11)-防作弊次数"
            + "<br>dominateScreen-int(11)-霸屏考试(0:是 1:否)"
            + "<br>calculator-int(11)-科学计算器(0:是 1:否)"
            + "<br>formula-int(11)-公式编辑器(0:是 1:否)"
            + "<br>resultsView-int(11)-成绩查看(0:不允许 1:允许 2:可查对错 3:可查答案)"
            + "<br>examinationComments_use-int(11)-启用考试评语(0:是 1:否)"
            + "<br>examinationComments-varchar(500)-考试评语(JSONString)"
            + "<br>wrongQuestions-int(11)-判分后错题加入错题集(0:是 1:否)"
            + "<br>selfGrade-int(11)-主观题自评分(0:是 1:否)"
            + "<br>deleteFlag-int(11)-删除标识(0:未删除 1:已删除)"
            + "<br>createTime-yyyy-MM-dd HH:mm:ss-创建时间"
            + "<br>updateTime-yyyy-MM-dd HH:mm:ss-修改时间")
    @ApiImplicitParam(name = "paperId", value = "即试卷主键pk_id", defaultValue = "0")
    public Result get(@RequestParam(required = false, defaultValue = "0") Integer paperId) {
        return examinationService.get(paperId);
    }
    @PostMapping(value = "/update")
    @RequiresPermissions("paper:set")
    @ApiOperation(value = "根据考试pkId,更新考试设置", notes = "code-{0-成功,1-失败},msg-结果说明,count-数据条数,data返回简单提示:")
    public Result update(@RequestBody @ApiParam(name = "examinationDTO", value = "考试表dto") ExaminationDTO examinationDTO) {
        return examinationService.update(examinationDTO);
    }
}
