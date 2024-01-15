package cn.itcouarge.platform.rmqclient.core.impl.ordinary;

import cn.itcouarge.platform.rmqclient.core.Producer;
import cn.itcouarge.platform.rmqclient.core.ProducerMessage;
import cn.itcouarge.platform.rmqclient.core.SendCallback;
import cn.itcouarge.platform.rmqclient.core.SendResult;
import cn.itcouarge.platform.rmqclient.core.config.ProducerConfig;
import cn.itcouarge.platform.rmqclient.core.exception.MQClientException;
import cn.itcouarge.platform.rmqclient.core.utils.MQUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 普通消息生产者实现类<BR/>
 * 张严  2018/9/26 16:52
 */
public class ProducerImpl implements Producer {

    private static final Logger logger = LoggerFactory.getLogger(ProducerImpl.class);

    private AtomicBoolean started = new AtomicBoolean(false);

    private ProducerConfig producerConfig;

    private DefaultMQProducer producer;

    public ProducerImpl(ProducerConfig producerConfig, String groupName) {
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
                logger.info("OrdinaryProducer start success " + producerConfig);
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
     * 同步发送
     *
     * @param message：生产者消息
     */
    public SendResult send(final ProducerMessage message) {
        SendResult sendResult = null;
        org.apache.rocketmq.common.message.Message msg = MQUtil.msgConvert(message);
        int timesTotal = producer.getRetryTimesWhenSendFailed();
        Exception exception = null;
        for (int time = 0; time < timesTotal; time++) {
            try {
                sendResult = MQUtil.sendResultConvert(producer.send(msg));
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

    /**
     * sendOneway发送
     *
     * @param message：生产者消息
     */
    public void sendOneway(ProducerMessage message) {
        try {
            org.apache.rocketmq.common.message.Message msg = MQUtil.msgConvert(message);
            producer.sendOneway(msg);
        } catch (Exception e) {
            logger.error("生产者" + producerConfig.toString() + "sendOneway失败", e);
            throw new MQClientException("生产者sendOneway失败", e);
        }
    }

    /**
     * 异步发送
     *
     * @param message：生产者消息
     * @param sendCallback：异步回调方法
     */
    public void sendAsync(ProducerMessage message, SendCallback sendCallback) {
        try {
            org.apache.rocketmq.common.message.Message msg = MQUtil.msgConvert(message);
            producer.send(msg, MQUtil.sendCallbackConvert(message, sendCallback));
        } catch (Exception e) {
            logger.error("生产者" + producerConfig.toString() + "sendAsync失败", e);
            throw new MQClientException("生产者sendAsync失败", e);
        }
    }

    /**
     * 批量发送数据
     *
     * @param message：生产者消息
     * @param sendCallback：异步回调方法
     */
    public void batchSend(List<ProducerMessage> message) {
        try {
            List<org.apache.rocketmq.common.message.Message> msg = MQUtil.msgConvert(message);
            producer.send(msg);
        } catch (Exception e) {
            logger.error("生产者" + producerConfig.toString() + "sendOneway失败", e);
            throw new MQClientException("生产者sendOneway失败", e);
        }
    }

}
