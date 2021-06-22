package com.njbandou.web.shiro;

import com.njbandou.web.JWTUtils;
import com.njbandou.web.constant.UserConstant;
import com.njbandou.web.entity.MenuEntity;
import com.njbandou.web.entity.RoleEntity;
import com.njbandou.web.entity.UserEntity;
import com.njbandou.web.mapper.RoleMapper;
import com.njbandou.web.mapper.RoleMenuMapper;
import com.njbandou.web.redis.JWTRedisDAO;
import com.njbandou.web.repository.RoleMenuRepository;
import com.njbandou.web.repository.UserRepository;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.*;

public class AdminRealm extends AuthorizingRealm {

    static Logger logger = LoggerFactory.getLogger(AdminRealm.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private JWTRedisDAO jwtRedisDAO;

    @Autowired
    private RoleMenuMapper roleMenuMapper;


    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 校验权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        UserEntity userEntity = (UserEntity) principalCollection.getPrimaryPrincipal();

        List<String> userRoles = new ArrayList<>();
        List<Integer> roleIdList = new ArrayList();

        List<MenuEntity> menus;
        Set<String> menuURLs = new HashSet<>();

        if (null != userEntity) {
            //获取当前用户下拥有的所有角色列表
            List<RoleEntity> roles = roleMapper.findByUserId(userEntity.getPkId());

            roles.forEach(role -> {
                userRoles.add(role.getName());
                roleIdList.add(role.getPkId());
            });

            if (!roleIdList.isEmpty()) {
                menus = roleMenuMapper.findByRoleIds(roleIdList);
                menus.forEach(menu -> {
                    if (!StringUtils.isEmpty(menu.getPerms())) {
                        if (menu.getPerms().contains(",")) {
                            String[] permsArray = menu.getPerms().split(",");
                            menuURLs.addAll(Arrays.asList(permsArray));
                        } else {
                            menuURLs.add(menu.getPerms());
                        }
                    }
                });
            }

        } else {
            throw new AuthorizationException();
        }

        //为当前用户设置角色和权限
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(userRoles);
        authorizationInfo.addStringPermissions(menuURLs);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {

        Map claims = JWTUtils.getClaims(UserConstant.JWT_SECRET, (String) authenticationToken.getPrincipal());

        if (claims == null) {
            //没找到帐号
            throw new UnknownAccountException();
        }

        String loginAccount = (String) claims.get("account");
        UserEntity userEntity = userRepository.findByAccount(loginAccount).orElseThrow(UnknownAccountException::new);

        String token = jwtRedisDAO.get(UserConstant.USER_JWT_PREFIX + loginAccount);
        logger.info("token {}", authenticationToken.getPrincipal());
        if (token == null || !token.equals(authenticationToken.getPrincipal())) {
            throw new AuthorizationException();
        }

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，可以自定义实现
        return new SimpleAuthenticationInfo(
                //用户信息
                userEntity,
                authenticationToken.getPrincipal(),
                //realm name
                getName()
        );

    }
}
