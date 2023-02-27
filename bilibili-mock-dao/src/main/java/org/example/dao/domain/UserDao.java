package org.example.dao.domain;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    public Integer addUser(User user);

    void addUserInfo(UserInfo userInfo);

    UserInfo getUserInfoByUserId(Long userId);

    User getUserById(Long userId);

    public User getUserByPhone(String phone);
}
