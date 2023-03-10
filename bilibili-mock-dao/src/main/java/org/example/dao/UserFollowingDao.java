package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.domain.UserFollowing;

@Mapper
public interface UserFollowingDao {
    Integer deleteUserFollowing(
            @Param("userId") Long userId,
            @Param("followedUserId") Long followedUserId);

    void addUserFollowing(UserFollowing userFollowing);
}
