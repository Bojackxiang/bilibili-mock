package org.example.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.User;
import org.example.domain.UserInfo;
import java.util.Map;

@Mapper
public interface UserDao {
    public Integer addUser(User user);

    void addUserInfo(UserInfo userInfo);

    UserInfo getUserInfoByUserId(Long userId);

    User getUserById(Long userId);

    public User getUserByPhone(String phone);

    void updateUserById(User user);

    // 当使用 JSONObject 的时候，不能直接放到 Map 中，需要转换成 Map
    Integer pageCountUserInfo(Map<String, Object> params);
}
