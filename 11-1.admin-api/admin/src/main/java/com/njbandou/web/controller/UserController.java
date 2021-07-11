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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Api(tags = "用于注册登录的相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @CrossOrigin
    @PostMapping(value = "/login")
    @ApiOperation(value = "用户登录", notes = "返回用户登录token", httpMethod = "POST")
    public Result login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO.getAccount(), userDTO.getPassword());
    }

    @PostMapping(value = "/logout")
    @ApiOperation(value = "用户退出登录", notes = "无返回值", httpMethod = "POST")
    public Result login(HttpSession session) {
        return ResultUtil.success(null);
    }

    @PostMapping(value = "/register")
    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    public Result register(@RequestBody UserDTO userDTO) {
        return userService.add(userDTO);
    }

    @CacheLock(prefix = "userAdd")
    @PostMapping(value = "/add")
    @RequiresPermissions("user:add")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，新增用户",notes = "返回“操作成功”")
    public Result add(@CacheParam @RequestBody UserDTO userDTO) {
        return userService.add(userDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("user:update")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，更新用户信息",notes = "返回“修改成功”")
    public Result update(@Valid @CacheParam @RequestBody UserDTO userDTO,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidateParamException(bindingResult);
        }

        return userService.update(userDTO);
    }

    @PostMapping(value = "/set/role")
    @RequiresPermissions("user:set:role")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，设置用户角色信息",notes = "返回“修改成功”")
    public Result setUserRole(@RequestBody UserDTO userDTO) {
        return userService.setUserRole(userDTO);
    }

    @CacheLock(prefix = "userResetPassword")
    @PostMapping(value = "/reset/password")
    @RequiresPermissions("user:reset:password")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，重置密码",notes = "返回“操作成功”")
    public Result update(@RequestBody UserDTO userDTO) {
        return userService.resetPassword(userDTO);
    }

    @CacheLock(prefix = "userUpdatePassword")
    @PostMapping(value = "/update/password")
    @RequiresPermissions("user:update:password")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，修改密码",notes = "返回“操作成功”")
    public Result updatePassword(@CacheParam @RequestBody UserDTO userDTO) {
        return userService.updatePassword(userDTO.getAccount(), userDTO.getPassword(), userDTO.getNewPassword());
    }

    @GetMapping(value = "/list")
    @RequiresPermissions("user:list")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，获取用户列表信息",notes = "分页返回用户列表")
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
    @ApiOperation(value = "shiro注解方式做权限检验通过后，通过pk-id获取用户信息",notes = "分页返回用户列表")
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
    @ApiOperation(value = "shiro注解方式做权限检验通过后，通过id检验用户是否存在",notes = "用户存在返回“操作成功”")
    @ApiImplicitParam(name = "userIds",value = "用户id数组")
    public Result verify(@RequestBody Integer[] userIds) {
        return userService.verify(userIds);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("user:delete")
    @ApiOperation(value = "shiro注解方式做权限检验通过后，通过id删除用户",notes = "用户存在返回“操作成功”")
    @ApiImplicitParam(name = "userIds",value = "用户id数组")
    public Result delete(@RequestBody Integer[] userIds) {
        return userService.delete(userIds);
    }

    @GetMapping(value = "/getCurrentInfo")
    @ApiOperation(value = "获取当前用户信息",notes = "返回格式：\n" +
            "pkId-用户主键\n" +
            "account-账号\n" +
            "nickname-昵称\n" +
            "avatar-头像\n" +
            "access-权限列表\n")
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
    @ApiOperation(value = "判断账号与密码是否匹配",notes = "匹配则返回成功")
    public Result unlock(@RequestBody UserDTO userDTO) {
        return userService.unlock(userDTO.getAccount(), userDTO.getPassword());
    }

}
