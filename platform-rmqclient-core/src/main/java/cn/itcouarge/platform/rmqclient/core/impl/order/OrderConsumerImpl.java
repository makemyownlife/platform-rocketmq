package cn.itcouarge.platform.rmqclient.core.impl.order;

import cn.itcouarge.platform.rmqclient.core.Consumer;
import cn.itcouarge.platform.rmqclient.core.ConsumerAction;
import cn.itcouarge.platform.rmqclient.core.ConsumerListener;
import cn.itcouarge.platform.rmqclient.core.ConsumerMessage;
import cn.itcouarge.platform.rmqclient.core.config.ConsumerConfig;
import cn.itcouarge.platform.rmqclient.core.exception.MQClientException;
import cn.itcouarge.platform.rmqclient.core.utils.MQUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 顺序消息消费者实现类<BR/>
 * 张严  2018/9/26 16:52
 */
public class OrderConsumerImpl implements Consumer {

    private final static Logger logger = LoggerFactory.getLogger(OrderConsumerImpl.class);

    private AtomicBoolean started = new AtomicBoolean(false);

    private ConcurrentMap<String, ConsumerListener> subscribeTable = new ConcurrentHashMap<String, ConsumerListener>();

    private ConsumerConfig consumerConfig;

    private DefaultMQPushConsumer consumer;

    public OrderConsumerImpl(ConsumerConfig consumerConfig, String groupName) {
        this.consumerConfig = consumerConfig;
        if (null == this.consumerConfig) {
            throw new MQClientException("properties not set");
        }
        if (StringUtils.isEmpty(this.consumerConfig.getNameSrvAddress())) {
            throw new MQClientException("NameSrvAddress not set");
        }
        if (StringUtils.isEmpty(groupName)) {
            throw new MQClientException("ConsumerGroup not set");
        }
        try {
            consumer = new DefaultMQPushConsumer(groupName);
            consumer.setInstanceName(MQUtil.getInstanceName());
            consumer.setNamesrvAddr(consumerConfig.getNameSrvAddress());
            consumer.setMaxReconsumeTimes(consumerConfig.getMaxReconsumeTimes());
            consumer.setConsumeFromWhere(consumerConfig.getConsumeFromWhere());
            consumer.setMessageModel(consumerConfig.getMessageModel());
            consumer.setSuspendCurrentQueueTimeMillis(consumerConfig.getSuspendCurrentQueueTimeMillis());
            consumer.setConsumeTimeout(consumerConfig.getConsumeTimeout());
            consumer.setConsumeMessageBatchMaxSize(consumerConfig.getConsumeMessageBatchMaxSize());
        } catch (Exception e) {
            logger.error("消费者" + consumerConfig.toString() + "初始化失败:", e);
            throw new MQClientException("消费者初始化失败:", e);
        }
    }

    /**
     * 启动顺序消息消费者
     */
    @Override
    public void start() {
        if (this.started.compareAndSet(false, true)) {
            consumer.registerMessageListener(new MessageListenerOrderly() {
                @Override
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                    try {
                        context.setAutoCommit(true);
                        MessageExt messageExt = msgs.get(0);
                        ConsumerMessage msg = new ConsumerMessage(messageExt.getTopic(), messageExt.getKeys(), messageExt.getBody(), messageExt.getMsgId(), messageExt.getStoreTimestamp(), messageExt.getTags());
                        msg.setQueueId(messageExt.getQueueId());
                        ConsumerListener consumerListener = subscribeTable.get(msg.getTopic());
                        ConsumerAction action = consumerListener.consumer(msg);
                        if (action != null) {
                            switch (action) {
                                case CommitMessage:
                                    return ConsumeOrderlyStatus.SUCCESS; //直接消费
                                case ReconsumeLater:
                                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;//会在一段时间之后重试消费消息
                                default:
                                    break;
                            }
                        }
                        return null;
                    } catch (Exception e) {
                        logger.error("消费者" + consumerConfig.toString() + "消费失败:", e);
                        throw new MQClientException("消费者消费失败:", e);
                    }

                }
            });

            try {
                consumer.start();
                logger.info("OrderConsumer start success " + consumerConfig);
            } catch (org.apache.rocketmq.client.exception.MQClientException e) {
                logger.error("消费者" + consumerConfig.toString() + "启动失败:", e);
                throw new MQClientException("消费者启动失败:", e);
            }
        }
    }

    /**
     * 关闭该consumer
     */
    @Override
    public void shutdown() {
        if (this.started.compareAndSet(true, false)) {
            if (this.consumer != null) {
                this.consumer.shutdown();
            }
        }
    }

    /**
     * 消费者订阅主题和相应的消费监听器
     *
     * @param topic:消费主题
     * @param listener：该主题对应的消费监听器
     */
    @Override
    public void subscribe(final String topic, final ConsumerListener listener) {
        if (null == topic) {
            throw new MQClientException("topic is null");
        }
        if (null == listener) {
            throw new MQClientException("listener is null");
        }
        this.subscribeTable.put(topic, listener);
        try {
            this.consumer.subscribe(topic, consumerConfig.getSubExpression());
        } catch (Exception e) {
            logger.error("消费者订阅" + topic + "失败：", e);
            throw new MQClientException(String.format("消费者订阅%s失败:", topic), e);
        }
    }

    /**
     * 消费者取消订阅主题和相应的消费监听器
     *
     * @param topic:消费主题
     */
    @Override
    public void unsubscribe(String topic) {
        if (null != topic) {
            try {
                this.subscribeTable.remove(topic);
                this.consumer.unsubscribe(topic);
            } catch (Exception e) {
                logger.error("消费者取消订阅" + topic + "失败：", e);
                throw new MQClientException("消费者订阅" + topic + "失败:", e);
            }
        }
    }

}