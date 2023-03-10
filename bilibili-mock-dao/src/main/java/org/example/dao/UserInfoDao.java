package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.domain.UserInfo;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserInfoDao {
    public Integer updateUserInfo(UserInfo userInfo);

    List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList);
}
