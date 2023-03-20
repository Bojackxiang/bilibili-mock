package org.example.service;

import org.example.dao.AuthRoleElementOperationDao;
import org.example.domain.authorities.AuthRoleElementOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 用户对于页面元素操作系统的权限
 */
@Service
public class AuthRoleElementOperationService {

    @Autowired
    private AuthRoleElementOperationDao authRoleElementOperationDao;


    public List<AuthRoleElementOperation> getRoleElementOperationByRoleIdSet(Set<Long> roleIdSet) {
        return authRoleElementOperationDao.getRoleElementOperationByRoleIdSet(roleIdSet);
    }
}
