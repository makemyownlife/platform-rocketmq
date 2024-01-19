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
    public OrderProducer createProducer() {
        ProducerConfig producerConfig = new ProducerConfig();
        producerConfig.setNameSrvAddress("192.168.1.9:9876");
        producerConfig.setRetryTimesWhenSendFailed(3);
        OrderProducer orderProducer = MQFactory.createOrderProducer(producerConfig, "testProducer");
        orderProducer.start();
        return orderProducer;
    }

    @Bean
    public Consumer createC() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setNameSrvAddress("192.168.1.9:9876");
        Consumer consumer = MQFactory.createOrderConsumer(consumerConfig, "testConsumer");
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
