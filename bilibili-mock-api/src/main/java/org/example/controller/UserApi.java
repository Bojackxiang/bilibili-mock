package org.example.controller;

import org.example.dao.domain.JsonResponse;
import org.example.dao.domain.User;
import org.example.dao.domain.UserInfo;
import org.example.helpers.UserVerifyTokenHelper;
import org.example.service.UserService;
import org.example.utils.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserApi {

    @Autowired
    private UserService userService;

    @Autowired
    private UserVerifyTokenHelper userVerifyTokenHelper;

    @GetMapping("/rsa-pks")
    public JsonResponse<String> rsaPks() {
        // 这个是获取一个 public key 的字符串
        String key = RSAUtil.getPublicKeyStr();
        return new JsonResponse<String>(key);
    }

    @PostMapping("/register")
    public JsonResponse<String> register(@RequestBody User user) {
        // create a user by using user service
        userService.createUser(user);
        return new JsonResponse<String>("success");
    }


    @PostMapping("/user-tokens")
    public JsonResponse<String> userTokens(@RequestBody User user) {
        // create a user by using user service
        String userToken = userService.generateToken(user);
        return new JsonResponse<>(userToken);
    }


    @GetMapping("/users")
    public JsonResponse<User> getUserInfo() {
        Long userId = userVerifyTokenHelper.getCurrentUserIdByToken();
        User user = userService.getUserInfoByUserId(userId);
        return new JsonResponse<>(user);
    }


    @PutMapping("/update-profile")
    public JsonResponse updateUserInfo(@RequestBody UserInfo userInfoInput) {
        Long userId = userVerifyTokenHelper.getCurrentUserIdByToken();
        UserInfo userInfo = userService.updateUserInfo(userId, userInfoInput);

        return JsonResponse.success();
    }

}
