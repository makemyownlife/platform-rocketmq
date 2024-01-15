package cn.javayong.platform.rmqclient.core.bean;

import cn.javayong.platform.rmqclient.core.Consumer;
import cn.javayong.platform.rmqclient.core.ConsumerListener;
import cn.javayong.platform.rmqclient.core.config.ConsumerConfig;
import cn.javayong.platform.rmqclient.core.exception.MQClientException;
import cn.javayong.platform.rmqclient.core.impl.factory.MQFactory;
import cn.javayong.platform.rmqclient.core.impl.order.OrderConsumerImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 顺序消息消费者bean,用于Spring容器的配置<BR/>
 * 张严  2018/9/26 16:52
 */
public class OrderConsumerBean implements Consumer {

    private ConsumerConfig consumerConfig;

    private String groupName;

    private OrderConsumerImpl consumer;

    private Map<String, ConsumerListener> subscribeTable = new HashMap<String, ConsumerListener>();

    /**
     * 启动一个消费者实例
     */
    @Override
    public void start() {
        if (null == this.consumerConfig) {
            throw new MQClientException("没有设置配置信息！");
        }
        this.consumer = MQFactory.createOrderConsumer(this.consumerConfig, this.groupName);
        if (this.subscribeTable != null) {
            synchronized (this) {
                //加载topic和监听器
                Set<String> set = this.subscribeTable.keySet();
                for (String key : set) {
                    this.subscribe(key, this.subscribeTable.get(key));
                }
                //启动消费者
                if (this.consumer != null) {
                    this.consumer.start();
                }
            }
        }
    }

    /**
     * 关闭当前消费者实例
     */
    @Override
    public void shutdown() {
        if (this.consumer != null) {
            this.consumer.shutdown();
        }
    }

    /**
     * 当前消费者添加topic订阅和相应的消费者监听器
     */
    @Override
    public void subscribe(final String topic, final ConsumerListener listener) {
        this.consumer.subscribe(topic, listener);
    }

    /**
     * 当前消费者取消topic订阅和相应的消费者监听器
     */
    @Override
    public void unsubscribe(final String topic) {
        this.consumer.unsubscribe(topic);
    }

    public ConsumerConfig getConsumerConfig() {
        return consumerConfig;
    }

    public void setConsumerConfig(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    public Map<String, ConsumerListener> getSubscribeTable() {
        return subscribeTable;
    }

    public void setSubscribeTable(Map<String, ConsumerListener> subscribeTable) {
        this.subscribeTable = subscribeTable;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
