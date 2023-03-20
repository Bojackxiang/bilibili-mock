package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.domain.authorities.UserRole;

import java.util.List;

@Mapper
public interface UserRoleDao {
    List<UserRole> getUserRoleByUserId(Long userId);

    Integer addUserRole(UserRole userRole);

}
