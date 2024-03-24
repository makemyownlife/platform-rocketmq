package cn.itcourage.platform.rmqclient.core.utils;


import cn.itcourage.platform.rmqclient.core.ProducerContext;
import cn.itcourage.platform.rmqclient.core.ProducerMessage;
import cn.itcourage.platform.rmqclient.core.SendResult;
import cn.itcourage.platform.rmqclient.core.SendStatus;
import cn.itcourage.platform.rmqclient.core.exception.MQClientException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * MQ工具类<BR/>
 * 张严  2018/9/26 16:52
 */
public class MQUtil {

    private static AtomicLong cmd = new AtomicLong(0);

    /**
     * 将自定义message转化为rocketmq的官方message
     *
     * @param message:生产者消息实体
     */
    public static Message msgConvert(final ProducerMessage message) {
        Message msgRMQ = new Message();
        if (message == null) {
            throw new MQClientException("message is null");
        }

        if (message.getTopic() != null) {
            msgRMQ.setTopic(message.getTopic());
        }
        if (message.getKey() != null) {
            msgRMQ.setKeys(message.getKey());
        }
        if (message.getTag() != null) {
            msgRMQ.setTags(message.getTag());
        }

        if (message.getBody() != null) {
            msgRMQ.setBody(message.getBody());
        }

        return msgRMQ;
    }

    /**
     * 将自定义List message转化为rocketmq的官方List message
     *
     * @param message:生产者消息实体
     */
    public static List<Message> msgConvert(final List<ProducerMessage> messages) {
        List<Message> msgRMQ = new ArrayList<Message>(messages.size());
        if (CollectionUtils.isEmpty(messages)) {
            throw new MQClientException("message is null");
        }
        messages.stream().forEach(t -> {
            Message msg = new Message();
            if (t.getTopic() != null) {
                msg.setTopic(t.getTopic());
            }
            if (t.getKey() != null) {
                msg.setKeys(t.getKey());
            }
            if (t.getTag() != null) {
                msg.setTags(t.getTag());
            }
            if (t.getBody() != null) {
                msg.setBody(t.getBody());
            }
            msgRMQ.add(msg);
        });
        return msgRMQ;
    }


    /**
     * 将自定义sendCallback嵌套到官方sendCallback中进行执行，获取数据
     *
     * @param message：生产者消息实体
     * @param sendCallback：生产者回调方法
     */
    public static SendCallback sendCallbackConvert(final ProducerMessage message,
                                                   final cn.itcourage.platform.rmqclient.core.SendCallback sendCallback) {
        SendCallback rmqSendCallback = new SendCallback() {
            @Override
            public void onSuccess(org.apache.rocketmq.client.producer.SendResult sendResult) {
                sendCallback.onSuccess(sendResultConvert(sendResult));
            }

            @Override
            public void onException(Throwable e) {
                String topic = new String(message.getTopic());
                String msgId = new String(message.getKey());
                ProducerContext context = new ProducerContext(topic, msgId, new MQClientException(e));
                sendCallback.onException(context);
            }
        };
        return rmqSendCallback;
    }

    /**
     * 将官方rmqSendResult转化为自定义rmqSendResult
     *
     * @param rmqSendResult：RocketMQ客户端发送消息后的响应实体
     */
    public static SendResult sendResultConvert(
            final org.apache.rocketmq.client.producer.SendResult rmqSendResult) {
        SendResult sendResult = new SendResult(rmqSendResult.getMsgId(), rmqSendResult.getMessageQueue().getTopic());
        switch (rmqSendResult.getSendStatus()) {
            case SEND_OK:
                sendResult.setSendStatus(SendStatus.SEND_OK);
                break;
            case FLUSH_DISK_TIMEOUT:
                sendResult.setSendStatus(SendStatus.FLUSH_DISK_TIMEOUT);
                break;
            case FLUSH_SLAVE_TIMEOUT:
                sendResult.setSendStatus(SendStatus.FLUSH_SLAVE_TIMEOUT);
                break;
            case SLAVE_NOT_AVAILABLE:
                sendResult.setSendStatus(SendStatus.SLAVE_NOT_AVAILABLE);
                break;
            default:
                sendResult.setSendStatus(SendStatus.UNKNOWN_STATUS);
                break;
        }
        return sendResult;
    }

    public static long getCmd() {
        return cmd.incrementAndGet();
    }

    public static String getInstanceName() {
        return RemotingUtil.getLocalAddress() + "@" + UtilAll.getPid() + "@" + getCmd();
    }

}
