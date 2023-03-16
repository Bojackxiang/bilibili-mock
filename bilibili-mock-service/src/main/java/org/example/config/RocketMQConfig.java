package org.example.config;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.example.constant.UserMomentConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;


import java.util.List;

@Configuration
public class RocketMQConfig {

    @Value("${rocketmq.name.server.address}")
    private String nameServerAddress;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


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

                for (MessageExt msg : msgs) {
                    System.out.println("msg = " + msg);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        return consumer;

    }



}
