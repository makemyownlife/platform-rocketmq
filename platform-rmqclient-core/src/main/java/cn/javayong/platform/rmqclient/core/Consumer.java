package cn.javayong.platform.rmqclient.core;

/**
 * 消费者<BR/>
 * 张严  2018/9/26 16:52
 */
public interface Consumer {

    void start();

    void shutdown();

    void subscribe(final String topic, final ConsumerListener listener);

    void unsubscribe(String topic);

}
