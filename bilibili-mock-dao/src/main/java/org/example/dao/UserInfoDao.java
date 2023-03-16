package org.example.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.UserInfo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Mapper
public interface UserInfoDao {
    public Integer updateUserInfo(UserInfo userInfo);

    List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList);

    List<UserInfo> pageListUserInfo(Map<String, Object> params);
}
