package cn.javayong.platform.rmqclient.core.bean;

import cn.javayong.platform.rmqclient.core.Producer;
import cn.javayong.platform.rmqclient.core.ProducerMessage;
import cn.javayong.platform.rmqclient.core.SendCallback;
import cn.javayong.platform.rmqclient.core.SendResult;
import cn.javayong.platform.rmqclient.core.config.ProducerConfig;
import cn.javayong.platform.rmqclient.core.exception.MQClientException;
import cn.javayong.platform.rmqclient.core.impl.factory.MQFactory;
import cn.javayong.platform.rmqclient.core.impl.ordinary.ProducerImpl;

/**
 * 生产者bean,用于Spring容器的配置
 */
public class ProducerBean implements Producer {

    private ProducerImpl producer;

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
        this.producer = MQFactory.createProducer(this.producerConfig, this.groupName);
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
    public SendResult send(final ProducerMessage message) {
        return this.producer.send(message);
    }

    @Override
    public void sendOneway(ProducerMessage message) {
        this.producer.sendOneway(message);
    }

    @Override
    public void sendAsync(ProducerMessage message, SendCallback sendCallback) {
        this.producer.sendAsync(message, sendCallback);
    }

    /**
     * 异步发送消息
     */
    public void send(final ProducerMessage message, SendCallback sendCallback) {
        producer.sendAsync(message, sendCallback);
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
