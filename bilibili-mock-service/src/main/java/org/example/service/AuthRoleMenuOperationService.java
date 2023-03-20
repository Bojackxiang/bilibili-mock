package org.example.service;

import org.example.dao.AuthRoleMenuOperationDao;
import org.example.domain.authorities.AuthRoleMenu;
import org.example.domain.authorities.AuthRoleMenuOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthRoleMenuOperationService {

    @Autowired
    private AuthRoleMenuOperationDao authRoleMenuOperationDao;


    public List<AuthRoleMenu> getRoleMenuOperationByRoleIdSet(Set<Long> roleIdSet) {
        return authRoleMenuOperationDao.getRoleMenuOperationByRoleIdSet(roleIdSet);
    }
}
