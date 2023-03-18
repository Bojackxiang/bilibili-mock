package org.example.controller;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.example.Configs.AppConfig;
import org.example.domain.JsonResponse;
import org.example.domain.UserMoment;
import org.example.helpers.UserVerifyTokenHelper;
import org.example.service.UserMomentService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserMomentApi {

    @Autowired
    private UserMomentService userMomentService;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private UserVerifyTokenHelper userVerifyTokenHelper;

    @PostMapping("/moments/add-moment")
    public JsonResponse<String> addMoment(@RequestBody UserMoment userMoment) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        long userId;
        boolean developmentMode = appConfig.isDevelopment();
        if (developmentMode) {
            userId = 20;
        } else {
            userId = userVerifyTokenHelper.getCurrentUserIdByToken();
        }
        // add user moment
        userMoment.setUserId(userId);
        userMomentService.addMoment(userMoment);

        return JsonResponse.success("success");
    }

}