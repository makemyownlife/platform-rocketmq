package cn.itcouarge.platform.rmqclient.core;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 消息类<BR/>
 * 张严  2018/9/26 16:52
 */
public class ProducerMessage implements Serializable {

    private static final long serialVersionUID = -1385924326856188094L;

    private static final Charset charset = Charset.forName("UTF-8");

    private String topic;

    private byte[] body;

    private String tag;

    private String key;

    public ProducerMessage(String topic, String tag, byte[] body) {
        this.topic = topic;
        this.body = body;
        this.tag = tag;
    }

    public ProducerMessage(String topic, String tag, String body) {
        this.topic = topic;
        this.body = body.getBytes(charset);
        this.tag = tag;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Message{" +
                "topic='" + topic + '\'' +
                ", body=" + Arrays.toString(body) +
                ", tag='" + tag + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
