package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.ExaminationPaperEntity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ExaminationPaperDTO {
    private Integer pkId;
    private Integer paperId;
    private Integer isMark;

    public ExaminationPaperEntity mark(ExaminationPaperEntity examinationPaper){
        examinationPaper.setIsMark(isMark);
        examinationPaper.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return examinationPaper;
    }
}
