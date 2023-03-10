package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.domain.UserInfo;

@Mapper
public interface UserInfoDao {
    public Integer updateUserInfo(UserInfo userInfo);
}
