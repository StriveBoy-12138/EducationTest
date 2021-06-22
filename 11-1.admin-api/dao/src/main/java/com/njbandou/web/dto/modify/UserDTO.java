package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.UserEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class UserDTO {
    private Integer pkId;

    private Integer title = 0;

    private Integer type;

    @NotBlank(message = "账号不能为空")
    private String account;

    private String password = "123456";

    private String newPassword;

    private String nickname = "";

    private String avatar = "";

    private String phone = "";

    private Integer organizationId = 0;

    private String otherInfo = "{}";

    private Integer isEnabled = 0;

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
