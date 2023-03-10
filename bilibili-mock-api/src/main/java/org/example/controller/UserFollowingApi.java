package org.example.controller;

import org.example.domain.FollowingGroup;
import org.example.domain.JsonResponse;
import org.example.domain.UserFollowing;
import org.example.helpers.UserVerifyTokenHelper;
import org.example.service.FollowingGroupService;
import org.example.service.UserFollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserFollowingApi {

    @Autowired
    private UserFollowingService userFollowingService;

    @Autowired
    private UserVerifyTokenHelper userVerifyTokenHelper;

    @Autowired
    private FollowingGroupService followingGroupService;

    @GetMapping("/user/followings")
    public JsonResponse<List<FollowingGroup>> getFollowings() {
//        long userId = userVerifyTokenHelper.getCurrentUserIdByToken();
        System.out.println("userFollowingService = " + userFollowingService);
        userFollowingService.getUserFollowing(20);
        List<FollowingGroup> followings = userFollowingService.getUserFollowing(20);
        return new JsonResponse(followings);

    }

}
