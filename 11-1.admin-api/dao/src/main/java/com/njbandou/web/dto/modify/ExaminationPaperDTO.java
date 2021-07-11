package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.ExaminationPaperEntity;
import io.swagger.annotations.*;
import lombok.Data;

import java.sql.Timestamp;
@ApiModel(value = "考试试卷表单条数据dto", description = "从客户端，由用户传来的数据封装在此dto中")
@Data
public class ExaminationPaperDTO {
    @ApiModelProperty(name ="pk_id",value = "考试试卷表主键id")
    private Integer pkId;
    @ApiModelProperty(name ="paperId",value = "对应的试卷id")
    private Integer paperId;
    @ApiModelProperty(name ="isMark",value = "是否阅卷(0:是 1:否)")
    private Integer isMark;

    public ExaminationPaperEntity mark(ExaminationPaperEntity examinationPaper){
        examinationPaper.setIsMark(isMark);
        examinationPaper.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return examinationPaper;
    }
}
