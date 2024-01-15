package cn.itcouarge.platform.rmqclient.core;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 消息类<BR/>
 * 张严  2018/9/26 16:52
 */
public class ConsumerMessage implements Serializable {

    private final static long serialVersionUID = -1085924226856188094L;

    private final static Charset charset = Charset.forName("UTF-8");

    private String topic;

    private String key;

    private byte[] body;

    private String msgId;

    private long storeTimestamp;

    private String tags;

    private Integer queueId;

    public ConsumerMessage(String topic, String key, byte[] body, String msgId, long storeTimestamp, String tags) {
        this.topic = topic;
        this.key = key;
        this.body = body;
        this.msgId = msgId;
        this.storeTimestamp = storeTimestamp;
        this.tags = tags;
    }

    public ConsumerMessage(String topic, byte[] body, String msgId, long storeTimestamp, String tags) {
        this.topic = topic;
        this.body = body;
        this.msgId = msgId;
        this.storeTimestamp = storeTimestamp;
        this.tags = tags;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public long getStoreTimestamp() {
        return storeTimestamp;
    }

    public void setStoreTimestamp(long storeTimestamp) {
        this.storeTimestamp = storeTimestamp;
    }

    public void setQueueId(Integer queueId) {
        this.queueId = queueId;
    }

    public Integer getQueueId() {
        return queueId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "ConsumerMessage{" + "topic='" + topic + '\'' + ", body=" + Arrays.toString(body) + ", msgId='" + msgId + '\'' + ", storeTimestamp=" + storeTimestamp + ", tags='" + tags + '\'' + '}';
    }

}
