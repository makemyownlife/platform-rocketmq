package cn.itcouarge.platform.rmqclient.core;

/**
 * 消费者监听器<BR/>
 * 张严  2018/9/26 16:52
 */
public interface ConsumerListener {

    ConsumerAction consumer(ConsumerMessage msg);

}
