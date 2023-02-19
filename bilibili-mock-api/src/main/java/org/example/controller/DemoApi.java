package org.example.controller;

import org.example.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DemoApi {
    @Autowired
    private DemoService demoService;


    @GetMapping("/demo")
    public long query(Integer id) {
        return demoService.getDemo(id);
    }

    @GetMapping("/test")
    public long test() {
        return 1;
    }
}
