package org.example.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DemoDao {
    public Integer query(Integer id);
}
