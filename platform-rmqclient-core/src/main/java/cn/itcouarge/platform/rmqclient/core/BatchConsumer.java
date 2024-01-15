package cn.itcouarge.platform.rmqclient.core;

/**
 * 批量消费者接口
 * Created by zhangyong on 2019/4/23.
 */
public interface BatchConsumer {

    void start();

    void shutdown();

    /**
     * 订阅消息
     * @param topic 消息主题
     * @param listener 消息回调监听器
     */
    void subscribe(final String topic, final BatchConsumerListener listener);

    /**
     * 取消某个topic订阅
     *
     * @param topic 消息主题
     */
    void unsubscribe(final String topic);

}
