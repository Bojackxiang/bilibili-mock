package org.example.service;

import org.example.domain.authorities.AuthRoleElementOperation;
import org.example.domain.authorities.AuthRoleMenu;
import org.example.domain.authorities.AuthRoleMenuOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthRoleService {

    @Autowired
    private AuthRoleElementOperationService authRoleElementOperationService;

    @Autowired
    private AuthRoleMenuOperationService authRoleMenuOperationService;


    /**
     * 用户对元素操作的权限
     * @param roleIdSet
     * @return
     */
    public List<AuthRoleElementOperation> getRoleElementOperationByRoleIdSet(Set<Long> roleIdSet) {
        return authRoleElementOperationService.getRoleElementOperationByRoleIdSet(roleIdSet);
    }


    /**
     * 用户对菜单的权限
     * @param roleIdSet
     * @return
     */
    public List<AuthRoleMenu> getRoleMenuOperationByRoleIdSet(Set<Long> roleIdSet) {
        return authRoleMenuOperationService.getRoleMenuOperationByRoleIdSet(roleIdSet);
    }

}
