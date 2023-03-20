package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.domain.authorities.AuthRoleMenu;
import org.example.domain.authorities.AuthRoleMenuOperation;

import java.util.List;
import java.util.Set;

@Mapper
public interface AuthRoleMenuOperationDao {

    List<AuthRoleMenu> getRoleMenuOperationByRoleIdSet(Set<Long> roleIdSet);
}
