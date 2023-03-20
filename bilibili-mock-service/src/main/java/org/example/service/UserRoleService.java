package org.example.service;

import org.example.dao.UserRoleDao;
import org.example.domain.authorities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {
    @Autowired
    private UserRoleDao userRoleDao; // 为了拿到用户的角色

    public List<UserRole> getUserRoleByUserId(long userId) {
        return userRoleDao.getUserRoleByUserId(userId);

    }
}
