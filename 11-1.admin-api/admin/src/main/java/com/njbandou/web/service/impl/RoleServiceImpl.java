package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.RoleDTO;
import com.njbandou.web.dto.search.SearchRoleDTO;
import com.njbandou.web.entity.RoleEntity;
import com.njbandou.web.entity.RoleMenuEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.MenuMapper;
import com.njbandou.web.mapper.RoleMapper;
import com.njbandou.web.repository.MenuRepository;
import com.njbandou.web.repository.RoleMenuRepository;
import com.njbandou.web.repository.RoleRepository;
import com.njbandou.web.service.RoleService;
import com.njbandou.web.vo.PageResult;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.RoleListResult;
import com.njbandou.web.vo.item.RoleResult;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RoleMenuRepository roleMenuRepository;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public Result getPage(SearchRoleDTO searchRoleDTO) {
        QueryResults<RoleResult> queryResult = roleMapper.selectByPage(searchRoleDTO);

        return ResultUtil.success(new PageResult<>(queryResult.getResults(), queryResult.getTotal()));
    }

    @Override
    public Result getAll() {
        QueryResults<RoleListResult> queryResult = roleMapper.selectAll();
        return ResultUtil.success(queryResult.getResults());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(RoleDTO roleDTO) {
        RoleEntity role = new RoleEntity();
        role.setName(roleDTO.getName());
        role.setDes(roleDTO.getRemark());
        role.setCreateTime(new Timestamp(System.currentTimeMillis()));
        role.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        roleRepository.save(role);
        List list = new ArrayList();
        // 查找父节点
        for (Integer menuId : roleDTO.getMenuIds()) {
            list.add(menuMapper.findParentIdByPkId(menuId));
        }
        roleDTO.getMenuIds().addAll(list);

        Iterator<Integer> iterator = roleDTO.getMenuIds().iterator();
        while (iterator.hasNext()) {
            Integer id = iterator.next();
            if (id == 0) {
                iterator.remove();
            }
        }

        Set<Integer> allMenuIds = new HashSet<>();
        allMenuIds.addAll(roleDTO.getMenuIds());
        List<RoleMenuEntity> roleMenus = fromRequest(role.getPkId(), allMenuIds);
        if (!roleMenus.isEmpty()) {
            roleMenuRepository.saveAll(roleMenus);
        }

        return ResultUtil.successWithMessage("添加成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(RoleDTO roleDTO) {
        RoleEntity role = roleRepository.findById(roleDTO.getPkId()).orElseThrow(() -> new ServerException("对象不存在"));
        role.setName(roleDTO.getName());
        role.setDes(role.getDes());
        role.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        roleRepository.saveAndFlush(role);

        roleMenuRepository.deleteByRoleId(roleDTO.getPkId());

        // 查找父节点
        List list = new ArrayList();
        for (Integer menuId : roleDTO.getMenuIds()) {
            Integer id = menuMapper.findParentIdByPkId(menuId);
            list.add(id);
        }
        if (!list.isEmpty()) {
            roleDTO.getMenuIds().addAll(list);
        }
        Iterator<Integer> iterator = roleDTO.getMenuIds().iterator();
        while (iterator.hasNext()) {
            Integer id = iterator.next();
            if (id == 0) {
                iterator.remove();
            }
        }
        Set<Integer> allMenuIds = new HashSet<>();
        allMenuIds.addAll(roleDTO.getMenuIds());
        List<RoleMenuEntity> roleMenus = fromRequest(role.getPkId(), allMenuIds);
        if (!roleMenus.isEmpty()) {
            roleMenuRepository.saveAll(roleMenus);
        }

        return ResultUtil.success();
    }

    @Override
    public Result delete(Integer[] ids) {
        Arrays.asList(ids).forEach(id -> roleRepository.deleteById(id));
        return ResultUtil.success(null);
    }

    @Override
    public Map info(Integer roleId) {
        List<RoleMenuEntity> menus = roleMenuRepository.findByRoleId(roleId);
        Set<Integer> pkIds = new HashSet<>();
        menus.forEach(item -> pkIds.add(item.getMenuId()));
        Map map = new HashMap(16);
        map.put("menuIdList", pkIds);
        return map;
    }

    protected List<RoleMenuEntity> fromRequest(Integer roleId, Set<Integer> menuIds) {
        List<RoleMenuEntity> roleMenus = new ArrayList<>();
        RoleMenuEntity roleMenu;
        for (Integer menuId : menuIds) {
            roleMenu = new RoleMenuEntity();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenu.setCreateTime(new Timestamp(System.currentTimeMillis()));
            roleMenus.add(roleMenu);
        }
        return roleMenus;
    }

}
