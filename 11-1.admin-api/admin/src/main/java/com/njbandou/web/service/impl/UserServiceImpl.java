package com.njbandou.web.service.impl;

import com.njbandou.web.JWTUtils;
import com.njbandou.web.constant.UserConstant;
import com.njbandou.web.dto.modify.UserDTO;
import com.njbandou.web.dto.search.SearchUserDTO;
import com.njbandou.web.entity.UserEntity;
import com.njbandou.web.entity.UserRoleEntity;
import com.njbandou.web.enums.UserIsEnabled;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.UserMapper;
import com.njbandou.web.redis.JWTRedisDAO;
import com.njbandou.web.repository.UserRepository;
import com.njbandou.web.repository.UserRoleRepository;
import com.njbandou.web.service.UserService;
import com.njbandou.web.shiro.EncryptUtils;
import com.njbandou.web.vo.PageResult;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.InviteUserResult;
import com.njbandou.web.vo.item.UserResult;
import com.njbandou.web.vo.item.UserLoginResult;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JWTRedisDAO jwtRedisDAO;

    @Override
    public Result login(String account, String password) {
        UserEntity userEntity = userRepository.findByAccount(account).orElseThrow(() -> new ServerException("账号不存在"));

        if (!EncryptUtils.encrypt(password, userEntity.getSalt()).equals(userEntity.getPassword())) {
            return ResultUtil.error("密码错误");
        }

        if (userEntity.getIsEnabled() == UserIsEnabled.UNABLED.getValue()) {
            return ResultUtil.error("用户已被禁用");
        }

        Map user = new HashMap(16);
        user.put("pkId", String.valueOf(userEntity.getPkId()));
        user.put("account", account);
        user.put("time", System.currentTimeMillis());
        String token = JWTUtils.generateToken(UserConstant.JWT_SECRET, user);
        jwtRedisDAO.set(UserConstant.USER_JWT_PREFIX + account, token);

        Map resultMap = new HashMap(16);
        resultMap.put("token", token);

        return ResultUtil.success(resultMap);
    }

    @Override
    public Result getPage(SearchUserDTO searchUserDTO) {
        QueryResults<UserResult> queryResultDTO = userMapper.selectByPage(searchUserDTO);
        return ResultUtil.success(new PageResult<>(queryResultDTO.getResults(), queryResultDTO.getTotal()));
    }

    @Override
    public Result getInviteUserPage(SearchUserDTO searchUserDTO) {
        QueryResults<InviteUserResult> queryResultDTO = userMapper.selectInviteUserByPage(searchUserDTO);
        return ResultUtil.success(new PageResult<>(queryResultDTO.getResults(), queryResultDTO.getTotal()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(UserDTO userDTO) {
        if (userRepository.findByAccount(userDTO.getAccount()).isPresent()) {
            throw new ServerException("该账号已存在");
        }

        // 用户
        UserEntity user = userDTO.insert();
        user.setSalt(EncryptUtils.createSalt());
        user.setPassword(EncryptUtils.encrypt(userDTO.getPassword(), user.getSalt()));
        userRepository.save(user);

        // 用户角色
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUserId(user.getPkId());
        userRole.setRoleId(userDTO.getRoleId());
        userRole.setCreateTime(new Timestamp(System.currentTimeMillis()));
        userRole.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        userRoleRepository.save(userRole);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(UserDTO userDTO) {
        UserEntity user = userRepository.findById(userDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        Optional<UserEntity> otherUser = userRepository.findByAccount(userDTO.getAccount());

        if (otherUser.isPresent() && !user.getPkId().equals(otherUser.get().getPkId())) {
            return ResultUtil.error("该账号已存在！");
        }

        userRepository.saveAndFlush(userDTO.update(user));

        return ResultUtil.successWithMessage("修改成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result setUserRole(UserDTO userDTO) {
        UserRoleEntity userRole = userRoleRepository.findByUserId(userDTO.getPkId());
        userRole.setRoleId(userDTO.getRoleId());
        userRole.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        userRoleRepository.saveAndFlush(userRole);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result verify(Integer[] ids) {
        for (Integer id : ids) {
            UserEntity user = userRepository.findById(id)
                    .orElseThrow(() -> new ServerException("用户不存在"));
            user.setIsEnabled(0);
            user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            userRepository.saveAndFlush(user);
        }

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Integer[] ids) {
        for (Integer id : ids) {
            Optional<UserEntity> user = userRepository.findById(id);
            if (user.isPresent()) {
                UserRoleEntity userRole = userRoleRepository.findByUserId(user.get().getPkId());
                if (userRole.getRoleId() == 1) {
                    return ResultUtil.error("此账号无法删除");
                }
                userRepository.delete(user.get());
            }
        }

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result resetPassword(UserDTO userDTO) {
        UserEntity user = userRepository.findById(userDTO.getPkId())
                .orElseThrow(() -> new ServerException("用户不存在"));

        user.setSalt(EncryptUtils.createSalt());
        user.setPassword(EncryptUtils.encrypt(userDTO.getPassword(), user.getSalt()));
        user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        userRepository.saveAndFlush(user);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updatePassword(String account, String password, String newPassword) {
        UserEntity user = userRepository.findByAccount(account)
                .orElseThrow(() -> new ServerException("用户不存在"));
        String encryptPassword = EncryptUtils.encrypt(password, user.getSalt());

        if (!encryptPassword.equals(user.getPassword())) {
            return ResultUtil.error("原密码错误！");
        }

        String newSalt = EncryptUtils.createSalt();
        String savePassword = EncryptUtils.encrypt(newPassword, newSalt);
        user.setSalt(newSalt);
        user.setPassword(savePassword);
        user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        userRepository.saveAndFlush(user);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    public Result unlock(String account, String password) {
        UserEntity user = userRepository.findByAccount(account)
                .orElseThrow(() -> new ServerException("用户不存在"));
        String encryptPassword = EncryptUtils.encrypt(password, user.getSalt());

        if (!encryptPassword.equals(user.getPassword())) {
            return ResultUtil.error("密码错误,请重新输入！");
        }

        return ResultUtil.success();
    }

    @Override
    public Result getInfoByPkId(UserEntity user) {
        UserLoginResult userLoginResult = new UserLoginResult();
        userLoginResult.setPkId(user.getPkId());
        userLoginResult.setAccount(user.getAccount());
        userLoginResult.setNickname(user.getNickname());
        userLoginResult.setAvatar(user.getAvatar());

        // 获取权限
        List<String> accessList = userMapper.selectUserAccessInfoByPkId(user.getPkId());
        if (!accessList.isEmpty()) {
            Set<String> distinctList = new LinkedHashSet<>();
            distinctList.addAll(accessList);

            accessList.clear();
            accessList.addAll(distinctList);
        }
        userLoginResult.setAccess(accessList);

        return ResultUtil.success(userLoginResult);
    }
}
