package org.example.service;

import org.example.constant.AuthErrorEnum;
import org.example.constant.Constants;
import org.example.constant.GroupErrorEnum;
import org.example.constant.UserConstants;
import org.example.dao.UserFollowingDao;
import org.example.domain.FollowingGroup;
import org.example.domain.User;
import org.example.domain.UserFollowing;
import org.example.domain.UserInfo;
import org.example.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * 获取到当前用户的关注的所有人的信息
     *
     * @param userId
     * @return
     */
    public List<FollowingGroup> getUserFollowing(long userId) {
        // 获取到当前用户的关注的所有人的信息
        List<UserFollowing> userFollowingList = userFollowingDao.getUserFollowing(userId);
        // 将被关注的所有人的 id 拿到
        Set<Long> followedUserIdSet = userFollowingList.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
        System.out.println("followedUserIdSet = " + followedUserIdSet.toString());

        // 获取被关注的人 user_info 的信息
        if (followedUserIdSet.size() == 0) {
            throw new ConditionException(
                    GroupErrorEnum.GROUP_ERROR_NO_FOLLOWING.getCode(),
                    GroupErrorEnum.GROUP_ERROR_NO_FOLLOWING.getMessage());
        }
        // -- 被关注的人的list信息
        List<UserInfo> userInfoList = userService.getUserInfoByUserIds(followedUserIdSet);
        for (UserFollowing userFollowing : userFollowingList) {
            for (UserInfo userInfo : userInfoList) {
                if (userFollowing.getFollowingId().equals(userInfo.getUserId())) {
                    userFollowing.setUserInfo(userInfo);
                }
            }
        }

        // 对被关注的人进行分组 并且返回
        List<FollowingGroup> followingGroupList = followingGroupService.getUserFollowingGroup(userId);

        FollowingGroup allGroup = new FollowingGroup();
        allGroup.setName(Constants.FOLLOWING_GROUP_ALL);
        allGroup.setFollowingUserInfoList(userInfoList);
        List<FollowingGroup> result = new ArrayList<>();
        // 全部关注
        result.add(allGroup);


        for (FollowingGroup followingGroup : followingGroupList) {
            List<UserInfo> userInfoList1 = new ArrayList<>();
            for (UserFollowing userFollowing : userFollowingList) {
                if (userFollowing.getGroupId().equals(Long.valueOf(followingGroup.getType()))) {

                    userInfoList1.add(userFollowing.getUserInfo());
                }
            }
            followingGroup.setFollowingUserInfoList(userInfoList1);
            result.add(followingGroup);
        }

        return result;
    }

    public List<UserFollowing> getUserFans(Long userId) {
        // 获取粉丝列表并检查
        List<UserFollowing> userFans = userFollowingDao.getUserFans(userId);
        Set<Long> fansUserIdSet = userFans.stream().map(UserFollowing::getUserId).collect(Collectors.toSet());
        if (userFans.size() == 0) {
            throw new ConditionException(
                    GroupErrorEnum.GROUP_ERROR_NO_FOLLOWING.getCode(),
                    GroupErrorEnum.GROUP_ERROR_NO_FOLLOWING.getMessage());
        }

        // 获取关注列表
        List<UserFollowing> userFollowingList = userFollowingDao.getUserFollowing(userId);

        // 获取粉丝信息
        List<UserInfo> userInfoList = userService.getUserInfoByUserIds(fansUserIdSet);

        // 给粉丝进行 userInfo 的赋值
        for (UserFollowing userFan : userFans) {
            for (UserInfo userInfo : userInfoList) {
                if (userFan.getUserId().equals(userInfo.getUserId())) {
                    userFan.setUserInfo(userInfo);
                }
            }
            for (UserFollowing subscribe : userFollowingList) {
                if (subscribe.getFollowingId().equals(userFan.getUserId())) {
                    userFan.getUserInfo().setFollowed(true);
                }
            }

        }

        return userFans;
    }

    public Integer addFollowingGroup(FollowingGroup followingGroup) {
        followingGroup.setCreateTime(new Date());
        followingGroup.setType(Constants.FOLLOWING_USER_CREATED_TYPE);
        return followingGroupService.addFollowingGroup(followingGroup);
    }

    public List<FollowingGroup> getAllUserFollowingGroup(long userId) {
        return followingGroupService.getAllUserFollowingGroup(userId);
    }
}
