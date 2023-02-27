package org.example.service;

import org.example.dao.DemoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoService {
    @Autowired
    private DemoDao demoDao;

    public Integer getDemo(Integer id) {
        return demoDao.query(id);
    }
}
