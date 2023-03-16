package org.example.service;

import org.example.constant.Constants;
import org.example.dao.FollowingGroupDao;
import org.example.dao.UserFollowingDao;
import org.example.domain.FollowingGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FollowingGroupService {

    @Autowired
    private FollowingGroupDao followingGroupDao;

    public FollowingGroup getByType(Integer type) {
        return followingGroupDao.getByType(type);
    }

    public FollowingGroup getById(Long id) {
        return followingGroupDao.getById(id);
    }


    public List<FollowingGroup> getUserFollowingGroup(long userId) {
        return followingGroupDao.getUserFollowingGroup(userId);
    }


    public Integer addFollowingGroup(FollowingGroup followingGroup) {
        return followingGroupDao.addFollowingGroup(followingGroup);
    }

    public List<FollowingGroup> getAllUserFollowingGroup(long userId) {
        return followingGroupDao.getAllUserFollowingGroup(userId);
    }
}
