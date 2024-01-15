package cn.itcouarge.platform.rmqclient.core;

/**
 * 发送结果<BR/>
 * 张严  2018/9/26 16:52
 */
public class SendResult {

    public SendResult(String messageId, String topic) {
        this.messageId = messageId;
        this.topic = topic;
    }

    /**
     * 已发送消息的ID
     */
    private String messageId;

    /**
     * 已发送消息的主题
     */
    private String topic;

    private SendStatus sendStatus;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public SendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    @Override
    public String toString() {
        return "SendResult{" + "messageId='" + messageId + '\'' + ", topic='" + topic + '\'' + ", sendStatus=" + sendStatus + '}';
    }

}
