package cn.itcourage.platform.rmqclient.core;

public interface OrderProducer {


    void start();

    SendResult send(final ProducerMessage message, final String shardingKey);

    void shutdown();

}
