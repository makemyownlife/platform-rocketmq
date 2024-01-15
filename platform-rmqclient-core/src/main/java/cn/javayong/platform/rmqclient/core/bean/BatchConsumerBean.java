package cn.javayong.platform.rmqclient.core.bean;

import cn.javayong.platform.rmqclient.core.BatchConsumer;
import cn.javayong.platform.rmqclient.core.BatchConsumerListener;
import cn.javayong.platform.rmqclient.core.config.ConsumerConfig;
import cn.javayong.platform.rmqclient.core.exception.MQClientException;
import cn.javayong.platform.rmqclient.core.impl.factory.MQFactory;
import cn.javayong.platform.rmqclient.core.impl.ordinary.BatchConsumerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 批量消费(非顺序，并发)
 * Created by zhangyong on 2019/4/23.
 */
public class BatchConsumerBean implements BatchConsumer {

    private final static Logger logger = LoggerFactory.getLogger(BatchConsumerBean.class);

    private Map<String, BatchConsumerListener> subscribeTable = new HashMap<String, BatchConsumerListener>();

    private ConsumerConfig consumerConfig;

    private String groupName;

    private BatchConsumerImpl consumer;

    @Override
    public void start() {
        if (null == this.consumerConfig) {
            throw new MQClientException("没有设置配置信息！");
        }
        this.consumer = MQFactory.createBatchConsumer(this.consumerConfig, this.groupName);
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

    @Override
    public void shutdown() {
        if (this.consumer != null) {
            this.consumer.shutdown();
        }
    }

    @Override
    public void subscribe(String topic, BatchConsumerListener listener) {
        this.consumer.subscribe(topic, listener);
    }

    @Override
    public void unsubscribe(String topic) {
        this.consumer.unsubscribe(topic);
    }

    public ConsumerConfig getConsumerConfig() {
        return consumerConfig;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setConsumerConfig(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Map<String, BatchConsumerListener> getSubscribeTable() {
        return subscribeTable;
    }

    public void setSubscribeTable(Map<String, BatchConsumerListener> subscribeTable) {
        this.subscribeTable = subscribeTable;
    }

}
