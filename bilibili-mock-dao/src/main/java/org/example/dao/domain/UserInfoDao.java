package org.example.dao.domain;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoDao {
    public Integer updateUserInfo(UserInfo userInfo);
}
