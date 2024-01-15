package cn.javayong.platform.rmqclient.core.impl.order;

import cn.javayong.platform.rmqclient.core.Producer;
import cn.javayong.platform.rmqclient.core.ProducerMessage;
import cn.javayong.platform.rmqclient.core.SendResult;
import cn.javayong.platform.rmqclient.core.config.ProducerConfig;
import cn.javayong.platform.rmqclient.core.exception.MQClientException;
import cn.javayong.platform.rmqclient.core.utils.MQUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 顺序消息生产者实现类<BR/>
 * 张严  2018/9/26 16:52
 */
public class OrderProducerImpl implements Producer {

    private static final Logger logger = LoggerFactory.getLogger(OrderProducerImpl.class);

    private AtomicBoolean started = new AtomicBoolean(false);

    private ProducerConfig producerConfig;

    private DefaultMQProducer producer;

    public OrderProducerImpl(ProducerConfig producerConfig, String groupName) {
        this.producerConfig = producerConfig;
        if (null == this.producerConfig) {
            throw new MQClientException("properties not set");
        }
        if (StringUtils.isEmpty(this.producerConfig.getNameSrvAddress())) {
            throw new MQClientException("NameSrvAddress not set");
        }
        if (StringUtils.isEmpty(groupName)) {
            throw new MQClientException("ProducerGroup not set");
        }
        try {
            producer = new DefaultMQProducer(groupName);
            producer.setNamesrvAddr(producerConfig.getNameSrvAddress());
            producer.setSendMsgTimeout(producerConfig.getSendMsgTimeout());
            producer.setRetryTimesWhenSendAsyncFailed(producerConfig.getRetryTimesWhenSendAsyncFailed());
            producer.setRetryTimesWhenSendFailed(producerConfig.getRetryTimesWhenSendFailed());
            producer.setInstanceName(MQUtil.getInstanceName());
        } catch (Exception e) {
            logger.error("生产者" + producerConfig.toString() + "初始化失败:", e);
            throw new MQClientException("生产者初始化失败:", e);
        }
    }

    /**
     * 启动生产者
     */
    @Override
    public void start() {
        if (this.started.compareAndSet(false, true)) {
            try {
                producer.start();
                logger.info("OrderProducer start success " + producerConfig);
            } catch (Exception e) {
                logger.error("生产者" + producerConfig.toString() + "启动失败:", e);
                throw new MQClientException("生产者启动失败:", e);
            }
        }
    }

    /**
     * 关闭该producer
     */
    @Override
    public void shutdown() {
        if (this.started.compareAndSet(true, false)) {
            if (this.producer != null) {
                this.producer.shutdown();
            }
        }
    }

    /**
     * 顺序消息同步发送
     *
     * @param message：生产者消息
     */
    public SendResult send(ProducerMessage message, final String shardingKey) {
        SendResult sendResult = null;
        int timesTotal = producer.getRetryTimesWhenSendFailed();
        Exception exception = null;
        Message msg = MQUtil.msgConvert(message);
        for (int time = 0; time < timesTotal; time++) {
            try {
                sendResult = MQUtil.sendResultConvert(producer.send(msg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        int select = Math.abs(shardingKey.hashCode());
                        if (select < 0) {
                            select = 0;
                        }
                        return mqs.get(select % mqs.size());
                    }
                }, 0));
            } catch (MQBrokerException e) {
                exception = e;
                if (e.getResponseCode() == 2 && StringUtils.isNotEmpty(e.getErrorMessage()) && e.getErrorMessage().contains("TIMEOUT_CLEAN_QUEUE")) {
                    try {
                        /*等待超时自动唤醒*/
                        Thread.sleep(300);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    logger.error("【broker系统繁忙】消息发送失败,300ms进行重试操作【times={}】" + time);
                    continue;
                }
                logger.error("生产者" + producerConfig.toString() + "send失败", e);
                throw new MQClientException("生产者send失败", e);
            } catch (Exception e) {
                logger.error("生产者" + producerConfig.toString() + "send失败", e);
                throw new MQClientException("生产者send失败", e);
            }
            break;
        }
        if (sendResult == null) {
            throw new MQClientException("生产者send失败", exception);
        }
        return sendResult;
    }

}