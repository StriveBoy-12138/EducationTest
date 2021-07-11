package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.UserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@ApiModel(value = "用户对象DTO", description = "从客户端，由用户传入的数据封装在此DTO中")
@Data
public class UserDTO {
    @ApiModelProperty(value = "用户主键", name = "pkId")
    private Integer pkId;

    @ApiModelProperty(value = "标签id", name = "title")
    private Integer title = 0;

    @ApiModelProperty(value = "类型(0:管理员 1:子管理员 2:学生)", name = "type")
    private Integer type;

    @ApiModelProperty(value = "账号", name = "account")
    @NotBlank(message = "账号不能为空")
    private String account;

    @ApiModelProperty(value = "密码（默认为123456）", name = "password")
    private String password = "123456";

    @ApiModelProperty(value = "新的密码", name = "newPassword")
    private String newPassword;

    @ApiModelProperty(value = "昵称", name = "nickname")
    private String nickname = "";

    @ApiModelProperty(value = "头像", name = "avatar")
    private String avatar = "";

    @ApiModelProperty(value = "手机号", name = "phone")
    private String phone = "";

    @ApiModelProperty(value = "组织id", name = "organizationId")
    private Integer organizationId = 0;

    @ApiModelProperty(value = "自定义信息", name = "otherInfo")
    private String otherInfo = "{}";

    @ApiModelProperty(value = "是否启用(0:启用 1:禁用)", name = "isEnabled")
    private Integer isEnabled = 0;

    @ApiModelProperty(value = "角色id", name = "roleId")
    private Integer roleId = 0;

    private UserEntity set(UserEntity user){
        user.setAccount(account);
        user.setTitle(title);
        user.setNickname(nickname);
        user.setOrganizationId(organizationId);
        user.setOtherInfo(otherInfo);
        user.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return user;
    }

    public UserEntity insert(){
        UserEntity user = set(new UserEntity());
        user.setType(2);
        user.setAvatar(avatar);
        user.setPhone(phone);
        user.setIsEnabled(isEnabled);
        user.setDeleteFlag(0);
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return user;
    }

    public UserEntity update(UserEntity user){
        return set(user);
    }
}
