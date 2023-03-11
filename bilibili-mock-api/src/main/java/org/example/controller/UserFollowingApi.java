package org.example.controller;

import org.example.Configs.AppConfig;
import org.example.domain.FollowingGroup;
import org.example.domain.JsonResponse;
import org.example.domain.UserFollowing;
import org.example.helpers.UserVerifyTokenHelper;
import org.example.service.FollowingGroupService;
import org.example.service.UserFollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserFollowingApi {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private UserFollowingService userFollowingService;

    @Autowired
    private UserVerifyTokenHelper userVerifyTokenHelper;

    @Autowired
    private FollowingGroupService followingGroupService;

    @GetMapping("/user/followings")
    public JsonResponse<List<FollowingGroup>> getFollowings() {
        long userId = userVerifyTokenHelper.getCurrentUserIdByToken();
        System.out.println("userFollowingService = " + userFollowingService);
        userFollowingService.getUserFollowing(userId);
        List<FollowingGroup> followings = userFollowingService.getUserFollowing(20);
        return new JsonResponse(followings);

    }

    @GetMapping("/user/fans")
    public JsonResponse<List<FollowingGroup>> getUserFans() {
        long userId;
        boolean developmentMode = appConfig.isDevelopment();
        if (developmentMode) {
            userId = 17;
        } else {
            userId = userVerifyTokenHelper.getCurrentUserIdByToken();
        }

        List<UserFollowing> userFans = userFollowingService.getUserFans(Long.valueOf(userId));
        return new JsonResponse(userFans);
    }

    @PostMapping("/user/add-following")
    public JsonResponse addFollowing(@RequestBody UserFollowing userFollowing) {
        long userId;
        boolean developmentMode = appConfig.isDevelopment();
        if (developmentMode) {
            userId = 20;
        } else {
            userId = userVerifyTokenHelper.getCurrentUserIdByToken();
        }

        userFollowing.setUserId(userId);
        userFollowingService.addUserFollowing(userFollowing);
        return JsonResponse.success();
    }
}
