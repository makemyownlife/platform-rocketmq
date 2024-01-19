package cn.itcouarge.demo.mq;

import cn.javayong.platform.rmqclient.core.*;
import cn.javayong.platform.rmqclient.core.config.ConsumerConfig;
import cn.javayong.platform.rmqclient.core.config.ProducerConfig;
import cn.javayong.platform.rmqclient.core.impl.factory.MQFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public Consumer createC() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setNameSrvAddress("192.168.1.9:9876");
        Consumer consumer = MQFactory.createConsumer(consumerConfig, "testConsumer");
        consumer.subscribe("mytest", new ConsumerListener() {
            @Override
            public ConsumerAction consumer(ConsumerMessage msg) {
                byte[] body = msg.getBody();
                System.out.println("msg:" + new String(body));
                return ConsumerAction.CommitMessage;
            }
        });
        consumer.start();
        return consumer;
    }

}
