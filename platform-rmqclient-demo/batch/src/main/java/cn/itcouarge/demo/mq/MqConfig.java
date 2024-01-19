package cn.itcouarge.demo.mq;


import cn.javayong.platform.rmqclient.core.*;
import cn.javayong.platform.rmqclient.core.config.ConsumerConfig;
import cn.javayong.platform.rmqclient.core.config.ProducerConfig;
import cn.javayong.platform.rmqclient.core.impl.factory.MQFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MqConfig {

    @Bean
    public Producer createProducer() {
        ProducerConfig producerConfig = new ProducerConfig();
        producerConfig.setNameSrvAddress("192.168.1.9:9876");
        producerConfig.setRetryTimesWhenSendFailed(3);
        Producer producer = MQFactory.createProducer(producerConfig, "testProducer");
        producer.start();
        return producer;
    }

    @Bean
    public BatchConsumer createC() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setNameSrvAddress("192.168.1.9:9876");
        BatchConsumer consumer = MQFactory.createBatchConsumer(consumerConfig, "testConsumer");
        consumer.subscribe("mytest", new BatchConsumerListener() {

            @Override
            public ConsumerAction consumer(List<ConsumerMessage> consumerMessageList) {
                for (ConsumerMessage consumerMessage : consumerMessageList) {
                    System.out.println("message:" + new String(consumerMessage.getBody()));
                }
                return ConsumerAction.CommitMessage;
            }
        });
        consumer.start();
        return consumer;
    }

}
