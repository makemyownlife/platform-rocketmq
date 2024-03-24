package cn.itcourage.platform.rmqclient.core;

import cn.itcourage.platform.rmqclient.core.config.ConsumerConfig;
import cn.itcourage.platform.rmqclient.core.config.ProducerConfig;
import cn.itcourage.platform.rmqclient.core.impl.order.BatchOrderConsumerImpl;
import cn.itcourage.platform.rmqclient.core.impl.order.OrderConsumerImpl;
import cn.itcourage.platform.rmqclient.core.impl.order.OrderProducerImpl;
import cn.itcourage.platform.rmqclient.core.impl.ordinary.BatchConsumerImpl;
import cn.itcourage.platform.rmqclient.core.impl.ordinary.ConsumerImpl;
import cn.itcourage.platform.rmqclient.core.impl.ordinary.ProducerImpl;

/**
 * 工厂<BR/>
 * 张严  2018/9/26 16:52
 */
public interface MQFactoryAPI {

    ProducerImpl createProducer(ProducerConfig producerConfig, String groupName);

    ConsumerImpl createConsumer(ConsumerConfig consumerConfig, String groupName);

    OrderProducerImpl createOrderProducer(ProducerConfig producerConfig, String groupName);

    OrderConsumerImpl createOrderConsumer(ConsumerConfig consumerConfig, String groupName);

    BatchOrderConsumerImpl createBatchOrderConsumer(ConsumerConfig consumerConfig, String groupName);

    BatchConsumerImpl createBatchConsumer(ConsumerConfig consumerConfig, String groupName);

}
