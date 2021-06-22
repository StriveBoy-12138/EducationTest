package com.njbandou.web.vo.item;

import com.njbandou.web.entity.MenuEntity;
import lombok.Data;

@Data
public class MenuResult {

    private Integer pkId;

    private Integer parentId;

    private String title;

    private Integer deleteFlag;

    public static MenuResult fromMenu(MenuEntity menu) {

        MenuResult menuResult = new MenuResult();
        menuResult.setPkId(menu.getPkId());
        menuResult.setParentId(menu.getParentId());
        menuResult.setTitle(menu.getTitle());
        return menuResult;
    }


}
