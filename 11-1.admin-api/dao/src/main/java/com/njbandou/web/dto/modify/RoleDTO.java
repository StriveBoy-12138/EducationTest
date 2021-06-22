package com.njbandou.web.dto.modify;

import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {
    private Integer pkId;
    private String name;
    private String remark;
    private List<Integer> menuIds;
}
