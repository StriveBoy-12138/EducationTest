package com.njbandou.web.service;

import com.njbandou.web.dto.modify.UserDTO;
import com.njbandou.web.dto.search.SearchUserDTO;
import com.njbandou.web.entity.UserEntity;
import com.njbandou.web.vo.Result;

public interface UserService {
    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return Result
     */
    Result login(String username, String password);

    /**
     * 获取用户列表
     *
     * @param searchUserDTO 搜索内容
     * @return PageResult
     */
    Result getPage(SearchUserDTO searchUserDTO);

    /**
     * 获取已邀请学员列表
     *
     * @param searchUserDTO 搜索内容
     * @return PageResult
     */
    Result getInviteUserPage(SearchUserDTO searchUserDTO);

    /**
     * 添加用户
     *
     * @param userDTO 用户参数
     * @return Result
     */
    Result add(UserDTO userDTO);

    /**
     * 更新用户信息
     *
     * @param userDTO 用户参数
     * @return Result
     */
    Result update(UserDTO userDTO);

    /**
     * 设置用户角色
     *
     * @param userDTO 用户参数
     * @return Result
     */
    Result setUserRole(UserDTO userDTO);

    /**
     * 批量审核用户
     *
     * @param ids 主键数组
     * @return Result
     */
    Result verify(Integer[] ids);

    /**
     * 批量删除用户
     *
     * @param ids 主键数组
     * @return Result
     */
    Result delete(Integer[] ids);

    /**
     * 重置密码
     *
     * @param userDTO
     * @return Result
     */
    Result resetPassword(UserDTO userDTO);

    /**
     * 修改密码
     *
     * @param username    账号
     * @param password    旧密码
     * @param newPassword 新密码
     * @return Result
     */
    Result updatePassword(String username, String password, String newPassword);

    /**
     * 解除锁屏
     * @param username 账号
     * @param password 密码
     * @return Result
     */
    Result unlock(String username, String password);

    /**
     * @param userEntity 主键
     * @return Result
     */
    Result getInfoByPkId(UserEntity userEntity);
}
