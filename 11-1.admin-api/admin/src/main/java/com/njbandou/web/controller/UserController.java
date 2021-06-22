package com.njbandou.web.controller;

import com.njbandou.web.annotation.CacheLock;
import com.njbandou.web.annotation.CacheParam;
import com.njbandou.web.dto.modify.UserDTO;
import com.njbandou.web.dto.search.SearchUserDTO;
import com.njbandou.web.entity.UserEntity;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.UserService;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @CrossOrigin
    @PostMapping(value = "/login")
    public Result login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO.getAccount(), userDTO.getPassword());
    }

    @PostMapping(value = "/logout")
    public Result login(HttpSession session) {
        return ResultUtil.success(null);
    }

    @PostMapping(value = "/register")
    public Result register(@RequestBody UserDTO userDTO) {
        return userService.add(userDTO);
    }

    @CacheLock(prefix = "userAdd")
    @PostMapping(value = "/add")
    @RequiresPermissions("user:add")
    public Result add(@CacheParam @RequestBody UserDTO userDTO) {
        return userService.add(userDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("user:update")
    public Result update(@Valid @CacheParam @RequestBody UserDTO userDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidateParamException(bindingResult);
        }

        return userService.update(userDTO);
    }

    @PostMapping(value = "/set/role")
    @RequiresPermissions("user:set:role")
    public Result setUserRole(@RequestBody UserDTO userDTO) {
        return userService.setUserRole(userDTO);
    }

    @CacheLock(prefix = "userResetPassword")
    @PostMapping(value = "/reset/password")
    @RequiresPermissions("user:reset:password")
    public Result update(@RequestBody UserDTO userDTO) {
        return userService.resetPassword(userDTO);
    }

    @CacheLock(prefix = "userUpdatePassword")
    @PostMapping(value = "/update/password")
    @RequiresPermissions("user:update:password")
    public Result updatePassword(@CacheParam @RequestBody UserDTO userDTO) {
        return userService.updatePassword(userDTO.getAccount(), userDTO.getPassword(), userDTO.getNewPassword());
    }

    @GetMapping(value = "/list")
    @RequiresPermissions("user:list")
    public Result list(@RequestParam(required = false, defaultValue = "0") Integer title,
                       @RequestParam(required = false, defaultValue = "-1") Integer isEnabled,
                       @RequestParam(required = false, name = "organizationIds[]") Integer[] organizationIds,
                       @RequestParam(required = false, defaultValue = "") String keywords,
                       @RequestParam(required = false, defaultValue = "") String userAccount,
                       @RequestParam(required = false, defaultValue = "") String userName,
                       @RequestParam(required = false, name = "exceptUserIds[]") Integer[] exceptUserIds,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false, defaultValue = "1") Integer page) {
        SearchUserDTO searchUserDTO = new SearchUserDTO();
        searchUserDTO.setTitle(title);
        searchUserDTO.setIsEnabled(isEnabled);
        searchUserDTO.setOrganizationIds(organizationIds);
        searchUserDTO.setKeyword(keywords);
        searchUserDTO.setUserAccount(userAccount);
        searchUserDTO.setUserName(userName);
        searchUserDTO.setExceptUserIds(exceptUserIds);
        searchUserDTO.setPage(page);
        searchUserDTO.setPageSize(pageSize);
        return userService.getPage(searchUserDTO);
    }

    @GetMapping(value = "/invite/list")
    @RequiresPermissions("user:list")
    public Result InviteUserList(@RequestParam(required = false, name = "userIds[]") Integer[] userIds,
                                 @RequestParam(required = false, defaultValue = "") String userAccount,
                                 @RequestParam(required = false, defaultValue = "") String userName,
                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false, defaultValue = "1") Integer page) {
        SearchUserDTO searchUserDTO = new SearchUserDTO();
        searchUserDTO.setUserIds(userIds);
        searchUserDTO.setUserAccount(userAccount);
        searchUserDTO.setUserName(userName);
        searchUserDTO.setPage(page);
        searchUserDTO.setPageSize(pageSize);
        return userService.getInviteUserPage(searchUserDTO);
    }

    @PostMapping(value = "/verify")
    @RequiresPermissions("user:verify")
    public Result verify(@RequestBody Integer[] userIds) {
        return userService.verify(userIds);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("user:delete")
    public Result delete(@RequestBody Integer[] userIds) {
        return userService.delete(userIds);
    }

    @GetMapping(value = "/getCurrentInfo")
    public Result getCurrentInfo() {
        UserEntity user = (UserEntity) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            Result error = ResultUtil.error("登陆失效");
            error.setCode(401);
            return error;
        }

        return userService.getInfoByPkId(user);
    }

    @PostMapping(value = "/unlock")
    public Result unlock(@RequestBody UserDTO userDTO) {
        return userService.unlock(userDTO.getAccount(), userDTO.getPassword());
    }

}
