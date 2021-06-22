package com.njbandou.web.vo.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class KnowledgePointResult {
    private Integer pkId;
    private Integer typeId;
    private String typeName;
    private String name;
    private Integer authorId;
    private String authorName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
}
