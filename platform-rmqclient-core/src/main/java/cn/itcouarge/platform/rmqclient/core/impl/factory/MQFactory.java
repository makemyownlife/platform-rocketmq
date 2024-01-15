package cn.itcouarge.platform.rmqclient.core.impl.factory;

import cn.itcouarge.platform.rmqclient.core.MQFactoryAPI;
import cn.itcouarge.platform.rmqclient.core.config.ConsumerConfig;
import cn.itcouarge.platform.rmqclient.core.config.ProducerConfig;
import cn.itcouarge.platform.rmqclient.core.impl.order.BatchOrderConsumerImpl;
import cn.itcouarge.platform.rmqclient.core.impl.order.OrderConsumerImpl;
import cn.itcouarge.platform.rmqclient.core.impl.order.OrderProducerImpl;
import cn.itcouarge.platform.rmqclient.core.impl.ordinary.BatchConsumerImpl;
import cn.itcouarge.platform.rmqclient.core.impl.ordinary.ConsumerImpl;
import cn.itcouarge.platform.rmqclient.core.impl.ordinary.ProducerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * API工厂<BR/>
 * 张严  2018/9/26 16:52
 */
public class MQFactory {

    private static final Logger logger = LoggerFactory.getLogger(MQFactory.class);

    private static MQFactoryAPI hshcFactoryAPI;

    /**
     *  实现工厂实现类的单例模式
     */
    static {
        try {
            Class<?> hshcFactory = MQFactory.class.getClassLoader().loadClass("cn.itcouarge.platform.rmqclient.core.impl.factory.MQFactoryImpl");
            hshcFactoryAPI = (MQFactoryAPI) hshcFactory.newInstance();
        } catch (Exception e) {
            logger.error("static construct error:", e);
        }
    }

    /**
     * 工厂类创建生产者
     *
     * @param producerConfig：生产者配置类
     * @param groupName：生产者组名
     */
    public static ProducerImpl createProducer(ProducerConfig producerConfig, String groupName) {
        return hshcFactoryAPI.createProducer(producerConfig, groupName);
    }

    /**
     * 工厂类创建消费者
     *
     * @param consumerConfig：消费者配置类
     * @param groupName：消费者组名
     */
    public static ConsumerImpl createConsumer(ConsumerConfig consumerConfig, String groupName) {
        return hshcFactoryAPI.createConsumer(consumerConfig, groupName);
    }

    /**
     * 工厂类创建顺序消息生产者
     *
     * @param producerConfig：生产者配置类
     * @param groupName：生产者组名
     */
    public static OrderProducerImpl createOrderProducer(ProducerConfig producerConfig, String groupName) {
        return hshcFactoryAPI.createOrderProducer(producerConfig, groupName);
    }

    /**
     * 工厂类创建顺序消息消费者
     *
     * @param consumerConfig：消费者配置类
     * @param groupName：消费者组名
     */
    public static OrderConsumerImpl createOrderConsumer(ConsumerConfig consumerConfig, String groupName) {
        return hshcFactoryAPI.createOrderConsumer(consumerConfig, groupName);
    }

    /**
     * 工厂类创建顺序批量消息消费者
     *
     * @param consumerConfig：消费者配置类
     * @param groupName：消费者组名
     */
    public static BatchOrderConsumerImpl createBatchOrderConsumer(ConsumerConfig consumerConfig, String groupName) {
        return hshcFactoryAPI.createBatchOrderConsumer(consumerConfig, groupName);
    }


    /**
     * 工厂类创建非顺序（并发）批量消息消费者
     *
     * @param consumerConfig：消费者配置类
     * @param groupName：消费者组名
     */
    public static BatchConsumerImpl createBatchConsumer(ConsumerConfig consumerConfig, String groupName) {
        return hshcFactoryAPI.createBatchConsumer(consumerConfig, groupName);
    }

}