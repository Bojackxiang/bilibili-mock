package org.example.controller;

import com.alibaba.fastjson.JSONObject;
import org.example.Configs.AppConfig;
import org.example.domain.*;
import org.example.helpers.UserVerifyTokenHelper;
import org.example.service.FollowingGroupService;
import org.example.service.UserFollowingService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private UserService userService;

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

    @PostMapping("/user/add-user-folllowing-group")
    public JsonResponse<Integer> addUserFollowingGroup(@RequestBody FollowingGroup followingGroup) {
        long userId;
        boolean developmentMode = appConfig.isDevelopment();
        if (developmentMode) {
            userId = 17;
        } else {
            userId = userVerifyTokenHelper.getCurrentUserIdByToken();
        }

        followingGroup.setUserId(userId);
        Integer newCreatedFollowingGroup = userFollowingService.addFollowingGroup(followingGroup);
        return new JsonResponse(newCreatedFollowingGroup);
    }


    /**
     * 获取某个用户当前全部的关注列表
     *
     * @return
     */
    @GetMapping("/user/following-groups")
    public JsonResponse<List<FollowingGroup>> getUserFollowingGroups() {
        long userId;
        boolean developmentMode = appConfig.isDevelopment();
        if (developmentMode) {
            userId = 17;
        } else {
            userId = userVerifyTokenHelper.getCurrentUserIdByToken();
        }

        List<FollowingGroup> userFollowingGroup = userFollowingService.getAllUserFollowingGroup(userId);
        return new JsonResponse(userFollowingGroup);
    }


    @GetMapping("/user/user-infos-v2")
    public JsonResponse<PageResults<UserInfo>> pageListUserInfo(
            @RequestParam int no,
            @RequestParam int size,
            @RequestParam String nick) {
        long userId;
        boolean developmentMode = appConfig.isDevelopment();
        if (developmentMode) {
            userId = 17;
        } else {
            userId = userVerifyTokenHelper.getCurrentUserIdByToken();
        }

        JSONObject params = new JSONObject();
        params.put("userId", userId);
        params.put("no", no);
        params.put("size", size);
        params.put("nick", nick);

        PageResults<UserInfo> result = userService.pageListUserInfos(params);

        return new JsonResponse<>(result);

    }
}
