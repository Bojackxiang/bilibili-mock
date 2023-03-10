package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.domain.User;
import org.example.domain.UserInfo;

@Mapper
public interface UserDao {
    public Integer addUser(User user);

    void addUserInfo(UserInfo userInfo);

    UserInfo getUserInfoByUserId(Long userId);

    User getUserById(Long userId);

    public User getUserByPhone(String phone);

    void updateUserById(User user);
}
