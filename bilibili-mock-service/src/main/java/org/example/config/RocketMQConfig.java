package org.example.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.example.constant.UserMomentConstants;
import org.example.domain.UserFollowing;
import org.example.domain.UserMoment;
import org.example.service.UserFollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;


import java.util.ArrayList;
import java.util.List;

@Configuration
public class RocketMQConfig {

    @Value("${rocketmq.name.server.address}")
    private String nameServerAddress;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserFollowingService userFollowingService;


    @Bean("momentsProducer")
    public DefaultMQProducer momentProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(UserMomentConstants.MOMENT_GROUP);
        producer.setNamesrvAddr(nameServerAddress);
        producer.start();
        return producer;
    }

    @Bean("momentConsumer")
    public DefaultMQPushConsumer momentConsumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UserMomentConstants.MOMENT_GROUP);
        consumer.setNamesrvAddr(nameServerAddress);
        consumer.subscribe(UserMomentConstants.MOMENT_TOPIC, "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println("=======>>> consumer starts <<<========");
                MessageExt msg = msgs.get(0);
                System.out.println("msg: " + msg);
                if(msg == null){
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }

                String bodyString = new String(msg.getBody());
                UserMoment userMoment = JSONObject.toJavaObject(
                        JSONObject.parseObject(bodyString),
                        UserMoment.class
                );

                // 用户发送动态，获取该用户的粉丝 id 列表
                Long userid = userMoment.getUserId();
                List<UserFollowing> followingIds = userFollowingService.getUserFans(userid);

                // ==>>> 为每一个粉丝发送一个信息 <<<==
                //      :将所有的信息发送到 redis 中，然后由 redis 的订阅者来进行消费
                for(UserFollowing fan: followingIds){
                    // 将动态存入 redis
                    String key = "subscribe:" + fan.getUserId();
                    String subscribedListString = redisTemplate.opsForValue().get(key);
                    List<UserMoment> subscribedList;
                    if(StringUtils.isNullOrEmpty(subscribedListString)){
                        subscribedList = new ArrayList<>();
                    }else{
                        subscribedList = JSONArray.parseArray(subscribedListString, UserMoment.class);
                    }
                    subscribedList.add(userMoment);
                    redisTemplate.opsForValue().set(key, JSONObject.toJSONString(subscribedList));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        return consumer;

    }



}
