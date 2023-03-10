package org.example.service;

import org.example.constant.AuthErrorEnum;
import org.example.constant.GroupErrorEnum;
import org.example.constant.UserConstants;
import org.example.dao.UserFollowingDao;
import org.example.domain.FollowingGroup;
import org.example.domain.User;
import org.example.domain.UserFollowing;
import org.example.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserFollowingService {

    @Autowired
    private UserFollowingDao userFollowingDao;

    @Autowired
    private FollowingGroupService followingGroupService;

    @Autowired
    private UserService userService;

    /**
     * 传捡来的是 一个被用户关注的用户
     * 用户要把这个人存到 default 分组中 还是要 存到别的分组中
     *
     * @param userFollowing
     */
    @Transactional
    public void addUserFollowing(UserFollowing userFollowing) {
        Long id = userFollowing.getGroupId();
        if (id == null) {
            FollowingGroup followingGroup = followingGroupService.getByType(UserConstants.USER_FOLLOWING_GROUP_TYPE_DEFAULT);
            userFollowing.setGroupId(followingGroup.getId());
        } else {
            FollowingGroup followingGroup = followingGroupService.getById(id); // 用户要把当前的用户存到哪个分组中
            if (followingGroup == null) {
                throw new ConditionException(
                        GroupErrorEnum.GROUP_ERROR_INVALID_GROUP_ID.getCode(),
                        GroupErrorEnum.GROUP_ERROR_INVALID_GROUP_ID.getMessage());
            }
        }

        Long followedUserId = userFollowing.getFollowingId();// 被关注的人的id
        User user = userService.getUserById(followedUserId);
        if (user == null) {
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_USER_NOT_EXISTED.getCode(),
                    AuthErrorEnum.AUTH_ERROR_USER_NOT_EXISTED.getMessage());
        }

        // 通过用户的自己的 id 和 被关注的人的id 来进行删除
        userFollowingDao.deleteUserFollowing(
                userFollowing.getUserId(),// 我的用户 id
                followedUserId // 我想关注的 用户 id
        );

        // 将更新的数据放到 在册放到 following 表中
        userFollowing.setCreateTime(new Date());
        userFollowingDao.addUserFollowing(userFollowing);


    }
}
