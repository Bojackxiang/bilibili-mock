package org.example.service;


import org.example.domain.authorities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserAuthService {

    @Autowired
    private UserRoleService userRoleService; // 为了拿到用户的角色

    @Autowired
    private AuthRoleService authRoleService; // 为了拿到角色的权限

    public UserAuthorities getUserAuthorities(long userId) {
        // 获取所有的 角色的 role id
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        Set<Long> roleIdSet = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());

        // 通过roleId 查看 role 关联的 UI 操作权限有哪些
        List<AuthRoleElementOperation> roleElementOperationList =
                authRoleService.getRoleElementOperationByRoleIdSet(roleIdSet);

        // 通过roleId 查看 role 关联的 MENU 权限有哪些
        List<AuthRoleMenu> roleMenuOperationList =
                authRoleService.getRoleMenuOperationByRoleIdSet(roleIdSet);

        // 构建返回参数
        UserAuthorities userAuthorities = new UserAuthorities();
        userAuthorities.setRoleElementOperationList(roleElementOperationList);
        userAuthorities.setRoleMenuList(roleMenuOperationList);

        return userAuthorities;
    }

}
