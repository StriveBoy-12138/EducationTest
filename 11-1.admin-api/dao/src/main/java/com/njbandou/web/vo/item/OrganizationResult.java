package com.njbandou.web.vo.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationResult {
    private Integer pkId;
    private Integer parentId;
    private Boolean expand;
    private String title;
    private List<OrganizationResult> children;
}
