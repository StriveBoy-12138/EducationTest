package com.njbandou.web.vo.item;

import com.njbandou.web.entity.MenuEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NavigationItemResult {

    private Integer pkId;

    private Integer parentId;

    private String title;

    private List<NavigationItemResult> children = new ArrayList<>();

    public static NavigationItemResult fromMenu(MenuEntity menu) {
        if (menu == null) {
            return null;
        }

        NavigationItemResult result = new NavigationItemResult();
        result.setPkId(menu.getPkId());
        result.setTitle(menu.getTitle());
        result.setParentId(menu.getParentId());

        return result;
    }
}
