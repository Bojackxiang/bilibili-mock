package org.example.controller;

import org.example.helpers.DemoHelper;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RestfulAPI {
    private final Map<Integer, Map<String, Object>> jsonMap;

    public RestfulAPI() {
        Map<Integer, Map<String, Object>> temp = new HashMap<>();

        for (int i = 0; i < 3; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", "user" + i);
            map.put("name", "username" + i);
            temp.put(i, map);
        }

        this.jsonMap = temp;
    }

    @GetMapping("/user/{id}")
    public Map<String, Object> getUser(@PathVariable("id") Integer id) {
        return jsonMap.get(id);
    }

    @DeleteMapping("/user/{id}")
    public Map<Integer, Map<String, Object>> deleteUser(@PathVariable("id") Integer id) {
        jsonMap.remove(id);
        return this.jsonMap;
    }

    @PostMapping("/user")
    public Map<Integer, Map<String, Object>> createUser(@RequestBody Map<String, Object> user) {
        // user validation
        String id = user.get("id").toString();
        boolean existed = DemoHelper.demoExistValidationV2(this.jsonMap, Integer.parseInt(id));
        if(!existed){
            throw new RuntimeException("user already existed");
        }
        jsonMap.put(jsonMap.size(), user);
        return this.jsonMap;
    }

    @PutMapping("/user")
    public Map<Integer, Map<String, Object>> putUser(@RequestBody Map<String, Object> user) {
        // user validation
        String id = user.get("id").toString();
        boolean existed = DemoHelper.demoExistValidationV2(this.jsonMap, Integer.parseInt(id));
        if(!existed){
            // create user data
            jsonMap.put(Integer.parseInt(id), user);
        }else{
            // 获取已经存在的用户
            Map<String, Object> existedUser = jsonMap.get(Integer.parseInt(id));
            // 传进去的是 reference
            DemoHelper.updateExistedUser(existedUser, user);
            // 更新原来的 json Map
            this.jsonMap.put(Integer.parseInt(id), existedUser);
        }
        return this.jsonMap;
    }
}
