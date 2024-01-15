package cn.javayong.platform.rmqclient.core;

import java.util.List;

/**
 * 批量监听器
 * Created by zhangyong on 2019/4/23.
 */
public interface BatchConsumerListener {

    ConsumerAction consumer(List<ConsumerMessage> consumerMessageList);

}
