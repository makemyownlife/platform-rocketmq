package cn.itcourage.platform.rmqclient.core.impl.factory;

import cn.itcourage.platform.rmqclient.core.MQFactoryAPI;
import cn.itcourage.platform.rmqclient.core.config.ConsumerConfig;
import cn.itcourage.platform.rmqclient.core.config.ProducerConfig;
import cn.itcourage.platform.rmqclient.core.impl.order.BatchOrderConsumerImpl;
import cn.itcourage.platform.rmqclient.core.impl.order.OrderConsumerImpl;
import cn.itcourage.platform.rmqclient.core.impl.order.OrderProducerImpl;
import cn.itcourage.platform.rmqclient.core.impl.ordinary.BatchConsumerImpl;
import cn.itcourage.platform.rmqclient.core.impl.ordinary.ConsumerImpl;
import cn.itcourage.platform.rmqclient.core.impl.ordinary.ProducerImpl;

/**
 * 工厂实现类<BR/>
 * 张严  2018/9/26 16:52
 */
public class MQFactoryImpl implements MQFactoryAPI {

    /**
     * 工厂类创建生产者
     *
     * @param producerConfig：生产者配置类
     * @param groupName：生产者组名
     */
    @Override
    public ProducerImpl createProducer(ProducerConfig producerConfig, String groupName) {
        return new ProducerImpl(producerConfig, groupName);
    }

    /**
     * 工厂类创建消费者
     *
     * @param consumerConfig：消费者配置类
     * @param groupName：消费者组名
     */
    @Override
    public ConsumerImpl createConsumer(ConsumerConfig consumerConfig, String groupName) {
        return new ConsumerImpl(consumerConfig, groupName);
    }

    /**
     * 工厂类创建顺序消息生产者
     *
     * @param producerConfig：生产者配置类
     * @param groupName：生产者组名
     */
    @Override
    public OrderProducerImpl createOrderProducer(ProducerConfig producerConfig, String groupName) {
        return new OrderProducerImpl(producerConfig, groupName);
    }

    /**
     * 工厂类创建顺序消息消费者
     *
     * @param consumerConfig：消费者配置类
     * @param groupName：消费者组名
     */
    @Override
    public OrderConsumerImpl createOrderConsumer(ConsumerConfig consumerConfig, String groupName) {
        return new OrderConsumerImpl(consumerConfig, groupName);
    }

    /**
     * 工厂类创建顺序批量消息消费者
     *
     * @param consumerConfig：消费者配置类
     * @param groupName：消费者组名
     */
    public BatchOrderConsumerImpl createBatchOrderConsumer(ConsumerConfig consumerConfig, String groupName) {
        return new BatchOrderConsumerImpl(consumerConfig, groupName);
    }

    /**
     * 工厂类创建批量消息消费者(非顺序,并发)
     *
     * @param consumerConfig：消费者配置类
     * @param groupName：消费者组名
     */
    public BatchConsumerImpl createBatchConsumer(ConsumerConfig consumerConfig, String groupName) {
        return new BatchConsumerImpl(consumerConfig, groupName);
    }

}