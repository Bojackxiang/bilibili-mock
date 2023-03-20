package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.domain.authorities.AuthRoleElementOperation;

import java.util.List;
import java.util.Set;

/**
 * 对于元素操作的 权限的 的DAO
 */
@Mapper
public interface AuthRoleElementOperationDao {

    public List<AuthRoleElementOperation> getRoleElementOperationByRoleIdSet(@Param("roleIdSet") Set<Long> roleIdSet);
}
