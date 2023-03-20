package org.example.controller;

import org.example.Configs.AppConfig;
import org.example.domain.JsonResponse;
import org.example.domain.authorities.UserAuthorities;
import org.example.helpers.UserVerifyTokenHelper;
import org.example.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAuthApi {

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private UserVerifyTokenHelper userVerifyTokenHelper;

    @Autowired
    private AppConfig appConfig;

    @GetMapping("/user-authorization")
    public JsonResponse<UserAuthorities> getUserAuthorization() {
        long userId;
        boolean developmentMode = appConfig.isDevelopment();
        if (developmentMode) {
            userId = 17;
        } else {
            userId = userVerifyTokenHelper.getCurrentUserIdByToken();
        }

        UserAuthorities userAuthorization = userAuthService.getUserAuthorities(userId);

        return new JsonResponse<>(userAuthorization);

    }


}