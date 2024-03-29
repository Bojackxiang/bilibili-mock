package org.example.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.example.constant.UserMomentConstants;
import org.example.dao.UserMomentDao;
import org.example.domain.UserMoment;
import org.example.utils.RocketMqUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class UserMomentService {

    @Autowired
    private UserMomentDao userMomentDao;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void addMoment(UserMoment userMoment) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        userMoment.setCreateTime(new Date());
        userMoment.setUpdateTime(new Date());

        userMomentDao.addUserMoment(userMoment);
        DefaultMQProducer defaultMQProducer =
                (DefaultMQProducer) applicationContext.getBean("momentsProducer");
        Message msg = new Message(
                UserMomentConstants.MOMENT_TOPIC,
                JSONObject.toJSONString(userMoment).getBytes(StandardCharsets.UTF_8)
        );
        RocketMqUtils.sendSyncMessage(defaultMQProducer, msg);
    }

    public List<UserMoment> getUserSubscribedMomentByUserId(long userId) {
        String key = "subscribe:" + userId;
        String subscribedList = redisTemplate.opsForValue().get(key);

        List<UserMoment> userMomentsList = JSONArray.parseArray(subscribedList, UserMoment.class);

        return userMomentsList;
    }
}
