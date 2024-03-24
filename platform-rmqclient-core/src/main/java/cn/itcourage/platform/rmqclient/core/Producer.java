package cn.itcourage.platform.rmqclient.core;

public interface Producer {

    void start();

    SendResult send(final ProducerMessage message);

    void sendOneway(ProducerMessage message);

    void sendAsync(ProducerMessage message, SendCallback sendCallback);

    void shutdown();

}
