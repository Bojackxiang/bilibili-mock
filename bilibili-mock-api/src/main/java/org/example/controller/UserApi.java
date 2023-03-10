package org.example.controller;

import org.example.constant.AuthErrorEnum;
import org.example.domain.JsonResponse;
import org.example.domain.User;
import org.example.domain.UserInfo;
import org.example.exception.ConditionException;
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

    @PutMapping("/user-details")
    public JsonResponse<String> updateUser(@RequestBody User updatedUser) {
        Long userId = userVerifyTokenHelper.verifyUserIdByToken();

        updatedUser.setId(userId);
        userService.updateUserById(updatedUser);

        return JsonResponse.success();
    }


    @PutMapping("/update-profile")
    public JsonResponse updateUserInfo(@RequestBody UserInfo userInfoInput) {
        // CHECK IF USERINFO WITH SPECIFIC USER ID EXISTS
        Long userId = userVerifyTokenHelper.getCurrentUserIdByToken();
        System.out.println("userId = " + userId);
        User user = userService.getUserInfoByUserId(userId);
        UserInfo userInfo = user.getUserInfo();
        System.out.println("userInfo = " + userInfo.toString());
        if (userInfo == null) {
            throw new ConditionException(
                    AuthErrorEnum.AUTH_ERROR_USER_INFO_NOT_EXISTED.getCode(),
                    AuthErrorEnum.AUTH_ERROR_USER_INFO_NOT_EXISTED.getMessage());
        }

        // UPDATE THE USER INFO CLASS AND THE DATABASE
        userInfoInput.setUserId(userId);
        userService.updateUserInfo(userInfoInput);
        return JsonResponse.success();
    }

}
