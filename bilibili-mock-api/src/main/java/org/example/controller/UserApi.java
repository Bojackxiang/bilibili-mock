package org.example.controller;

import org.example.dao.domain.JsonResponse;
import org.example.service.UserService;
import org.example.utils.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApi {

    @Autowired
    private UserService userService;

    @GetMapping("/rsa-pks")
    public JsonResponse<String> rsaPks() {
        // 这个是获取一个 public key 的字符串
        String key = RSAUtil.getPublicKeyStr();
        return new JsonResponse<String>(key);
    }


}
