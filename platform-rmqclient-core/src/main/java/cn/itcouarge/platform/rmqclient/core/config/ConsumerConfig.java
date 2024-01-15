package cn.itcouarge.platform.rmqclient.core.config;

import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * 消费者配置类<BR/>
 * 张严  2018/9/26 16:52
 */
public class ConsumerConfig {

    //NameSrv地址
    private String nameSrvAddress;

    //设置重试消费最大次数：超出则投递到死信队列中
    private int maxReconsumeTimes = 3;

    //消费起始位置：默认从开始位置消费
    private ConsumeFromWhere consumeFromWhere = ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET;

    //消费模式：默认集群模式
    private MessageModel messageModel = MessageModel.CLUSTERING;

    //tag过滤表达式
    private String subExpression = "*";

    //超时时间
    private long consumeTimeout = 15L;

    //轮询超时时间
    private long suspendCurrentQueueTimeMillis = 1000L;

    //每次消费消息数
    private int consumeMessageBatchMaxSize = 1;

    public String getNameSrvAddress() {
        return nameSrvAddress;
    }

    public void setNameSrvAddress(String nameSrvAddress) {
        this.nameSrvAddress = nameSrvAddress;
    }

    public int getMaxReconsumeTimes() {
        return maxReconsumeTimes;
    }

    public void setMaxReconsumeTimes(int maxReconsumeTimes) {
        this.maxReconsumeTimes = maxReconsumeTimes;
    }

    public String getSubExpression() {
        return subExpression;
    }

    public long getSuspendCurrentQueueTimeMillis() {
        return suspendCurrentQueueTimeMillis;
    }

    public void setSuspendCurrentQueueTimeMillis(long suspendCurrentQueueTimeMillis) {
        this.suspendCurrentQueueTimeMillis = suspendCurrentQueueTimeMillis;
    }

    public ConsumeFromWhere getConsumeFromWhere() {
        return consumeFromWhere;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }

    public long getConsumeTimeout() {
        return consumeTimeout;
    }

    public void setConsumeTimeout(long consumeTimeout) {
        this.consumeTimeout = consumeTimeout;
    }

    public int getConsumeMessageBatchMaxSize() {
        return consumeMessageBatchMaxSize;
    }

    @Override
    public String toString() {
        return "ConsumerConfig{" + "nameSrvAddress='" + nameSrvAddress + '\'' + ", MaxReconsumeTimes=" + maxReconsumeTimes + ", consumeFromWhere=" + consumeFromWhere + ", messageModel=" + messageModel + ", subExpression='" + subExpression + '\'' + ", consumeTimeout=" + consumeTimeout + ", SuspendCurrentQueueTimeMillis=" + suspendCurrentQueueTimeMillis + ", ConsumeMessageBatchMaxSize=" + consumeMessageBatchMaxSize + '}';
    }

}
