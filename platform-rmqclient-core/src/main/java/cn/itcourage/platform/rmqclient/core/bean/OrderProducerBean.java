package cn.itcourage.platform.rmqclient.core.bean;

import cn.itcourage.platform.rmqclient.core.OrderProducer;
import cn.itcourage.platform.rmqclient.core.ProducerMessage;
import cn.itcourage.platform.rmqclient.core.SendResult;
import cn.itcourage.platform.rmqclient.core.config.ProducerConfig;
import cn.itcourage.platform.rmqclient.core.exception.MQClientException;
import cn.itcourage.platform.rmqclient.core.impl.factory.MQFactory;
import cn.itcourage.platform.rmqclient.core.impl.order.OrderProducerImpl;

/**
 * 顺序消息生产者bean,用于Spring容器的配置<BR/>
 */
public class OrderProducerBean implements OrderProducer {

    private OrderProducerImpl producer;

    private ProducerConfig producerConfig;

    private String groupName;

    /**
     * 启动一个生产者实例
     */
    @Override
    public void start() {
        if (null == this.producerConfig) {
            throw new MQClientException("没有设置配置信息！");
        }
        this.producer = MQFactory.createOrderProducer(this.producerConfig, this.groupName);
        this.producer.start();
    }

    /**
     * 关闭当前生产者实例
     */
    @Override
    public void shutdown() {
        if (this.producer != null) {
            this.producer.shutdown();
        }
    }

    /**
     * 同步发送消息
     *
     * @param message：生产者同步发送的消息实体
     */
    public SendResult send(final ProducerMessage message, final String shardingKey) {
        return this.producer.send(message, shardingKey);
    }

    public ProducerConfig getProducerConfig() {
        return producerConfig;
    }

    public void setProducerConfig(ProducerConfig producerConfig) {
        this.producerConfig = producerConfig;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
