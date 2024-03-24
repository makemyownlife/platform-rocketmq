package org.apache.rocketmq.console;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;
import java.util.Random;

public class ProducerMain {

    public static void main(String[] args) throws MQClientException, MQClientException {
        //生产者组
        DefaultMQProducer producer = new DefaultMQProducer("mymac_producer_group");
        //生产者需用通过NameServer获取所有broker的路由信息，多个用分号隔开，这个跟Redis哨兵一样
        producer.setNamesrvAddr("localhost:9876;");
        //启动
        producer.start();

        for (int i = 0; i < 400000; i++) {
            try {
                /*Message(String topic, String tags, String keys, byte[] body)
                 Message代表一条信息，第一个参数是topic，这是主题
                第二个参数是tags，这是可选参数，用于消费端过滤消息
                第三个参数是keys，这也是可选参数，如果有多个，用空格隔开。RocketMQ可以根据这些key快速检索到消息，相当于
                消息的索引，可以设置为消息的唯一编号（主键）。*/
                Message msg = new Message("my-mac-topic", "TagA", "6666" + new Random(100), ("RocketMQ Test message " + i).getBytes());
                //SendResult是发送结果的封装，包括消息状态，消息id，选择的队列等等，只要不抛异常，就代表发送成功
                SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                        return list.get(0);
                    }
                } , 1111);
                System.out.println("第" + i + "条send结果: " + sendResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        producer.shutdown();
    }

}
