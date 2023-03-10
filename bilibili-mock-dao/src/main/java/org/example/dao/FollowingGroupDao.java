package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.domain.FollowingGroup;

@Mapper
public interface FollowingGroupDao {
    FollowingGroup getByType(long type);

    FollowingGroup getById(long id);
}
