2018 年，做为架构负责人，接到一个架构需求：实现一个简单易用的 RocketMQ SDK 。

因为各个团队 RocketMQ 原生客户端配置起来千奇百怪，有的配置存在风险，各团队负责人都需要一个简洁易用的 RocketMQ SDK 。

我立马调研相关开源的方案，当时 RocketMQ-Spring 项目并没有开源，而阿里云的 ONS SDK 是开源的，我只能讲目标转向 阿里云 ONS 。

通过学习 ONS 的设计方式，我对于 RocketMQ 的客户端原理有了进一步了解，同时参考 ONS 的设计，也实现了公司内部使用的 RocketMQ  SDK 。

![](https://javayong.cn/pics/rocketmq/platform-rocketmq-client.png?)

> 项目地址：https://github.com/makemyownlife/platform-rocketmq

之所以说简单，就是**让用户（开发者）使用 SDK 时，减少心智负担**。

举三个例子：

# 1 发送顺序消息

使用原生代码发送消息时，会使用如下的代码：

```java
SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
    @Override
    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
        Integer id = (Integer) arg;
        int index = id % mqs.size();
        return mqs.get(index);
    }
}, orderId);
```

我们可以将 SDK API 简化为：

```java
SendResult send(final ProducerMessage message, final String shardingKey);
```

开发者不需要定义队列选择器，只需要传递分片键 orderId 即可。

# 2 单条消息消费

使用原来代码定义消费监听器时，使用如下的代码：

```java
consumer.registerMessageListener(new MessageListenerConcurrently() {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
        // 返回消息消费状态，ConsumeConcurrentlyStatus.CONSUME_SUCCESS为消费成功
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
});
```

监听器内部，对于开发者操作的对象是消息列表 msgs ，很多开发同学想只操作一条消息。

于是，我们可以将 SDK API 简化为：

```
consumer.subscribe("mytest", new ConsumerListener() {
    @Override
    public ConsumerAction consumer(ConsumerMessage msg) {
        byte[] body = msg.getBody();
        System.out.println("msg:" + new String(body));
        return ConsumerAction.CommitMessage;
    }
});
```

开发者在消费时，可以一条一条操作，代码简洁了不少。

同时，很多开发者在使用普通消费、顺序消费时，需要返回延时消费的状态码时，两种消费模式定义的枚举也不相同。我们将枚举做了统一：

```java
/**
 * 消费消息的返回结果
 */
public enum ConsumerAction {

    /**
     * 消费成功，继续消费下一条消息
     */
    CommitMessage,
    
    /**
     * 消费失败，告知服务器稍后再投递这条消息，继续消费其他消息
     */
    ReconsumeLater;
}
```

# 3 订阅关系一致

实际场景里，订阅关系不一致是极容易发生的事情，就算是高级别的架构师也会翻车，每次翻车现场都是惨不忍睹。

正确的订阅关系见下图：

![正确的订阅关系](https://javayong.cn/pics/rocketmq/rightsub.png)

代码逻辑角度来看，**每个消费者实例内订阅方法的主题、 TAG、监听逻辑都需要保持一致**。

![](https://javayong.cn/pics/rocketmq/subrightcode.png)

**当订阅关系不一致时，在 Broker 端同一个消费组内的各个消费者客户端的订阅信息相互被覆盖，从而导致某个消费者客户端无法拉取到新的消息。**

怎么解决呢 ？

我当时想起了阿里技术专家沈询的一句话：

> 世界上解决一个计算机问题最简单的方法：“恰好”不需要解决它 ！

公司内部出现订阅关系一致99%的问题是：消费者组一致的前提下，主题相同，但 TAG 不相同。

基于此，我的设计思路就明确了：不开放订阅 TAG 的权限！

没想到吧，我就是这么粗暴。

按照这种设计思路，虽然开始有的程序员会有质疑，但你和他梳理好消费者组的定义，以及做好领域划分，对业务来讲，反而清晰了。

# 4 写到最后

我并不认为我们写得多么的好，只是想让同学们理解：

1、**成长的第一步就是模仿**；

**2、把开发者当用户，以用户的体验为先**。

我经常去阅读阿里云产品的 SDK  ,  去思考他们为什么这么设计， 为什么和开源的有所不同，有的时候开始没想明白，但模仿得多了，好像也慢慢懂了。

> 要紧的是果敢地迈出第一步，对与错先都不管，自古就没有把一切都设计好再开步的事。
>
> 别想把一切都弄清楚，再去走路。鲁莽者要学会思考，善思者要克服的是犹豫。
>
> ——史铁生

---

我的知识星球专栏 RocketMQ 三部曲持续更新中：

- RocketMQ 4.X 原理与实战 （基本完成）
- Raft 算法剖析 (准备中)
- 深入理解 RocketMQ 5.X  (准备中)

有兴趣的同学，可以关注下。

![](https://javayong.cn/pics/rocketmq/RocketMQ4X.png)**如何加入星球？**

**步骤1：添加我的微信（zhangyongtaozhe）**

![](https://javayong.cn/pics/shipinhao/weixinhao.png?a=23)

> 添加微信后，我们组织直播、送书活动时，微信沟通会更加便捷和高效。

**步骤2：微信扫一扫星球优惠券**

![](https://javayong.cn/pics/shipinhao/xingqiucoupon.png?a=123)





