package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.domain.FollowingGroup;
import org.example.domain.UserFollowing;

import java.util.List;

@Mapper
public interface UserFollowingDao {
    Integer deleteUserFollowing(
            @Param("userId") Long userId,
            @Param("followedUserId") Long followedUserId);

    void addUserFollowing(UserFollowing userFollowing);

    List<UserFollowing> getUserFollowing(long userId);

    List<UserFollowing> getUserFans(Long userId);
}
