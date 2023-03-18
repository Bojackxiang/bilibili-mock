package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.domain.UserMoment;

@Mapper
public interface UserMomentDao {

    public void addUserMoment(UserMoment userMoment);

}
