package com.njbandou.web.vo.item;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class NavigationResult {

    private List<NavigationItemResult> menuList = new ArrayList<>();

    private Set<String> permissions = new HashSet<>();

    public NavigationResult(List<NavigationItemResult> menuList, Set<String> permissions) {
        this.menuList = menuList;
        this.permissions = permissions;
    }
}
