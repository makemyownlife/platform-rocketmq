package cn.itcouarge.platform.rmqclient.core;

import cn.itcouarge.platform.rmqclient.core.exception.MQClientException;

/**
 * 生产者上下文类<BR/>
 * 张严  2018/9/26 16:52
 */
public class ProducerContext {

    private String topic;

    private String msgId;

    private MQClientException exception;

    public ProducerContext(String topic, String msgId, MQClientException exception) {
        this.topic = topic;
        this.msgId = msgId;
        this.exception = exception;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public MQClientException getException() {
        return exception;
    }

    public void setException(MQClientException exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "MQExceptionContext{" + "topic='" + topic + '\'' + ", msgId='" + msgId + '\'' + ", exception=" + exception + '}';
    }

}
