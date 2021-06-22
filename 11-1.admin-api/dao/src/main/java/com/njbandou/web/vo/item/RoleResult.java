package com.njbandou.web.vo.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class RoleResult {
    private Integer pkId;

    private String name;

    private String des;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
}
