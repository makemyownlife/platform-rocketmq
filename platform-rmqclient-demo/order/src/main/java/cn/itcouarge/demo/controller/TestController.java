package cn.itcouarge.demo.controller;

import cn.itcourage.platform.rmqclient.core.OrderProducer;
import cn.itcourage.platform.rmqclient.core.ProducerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class TestController {

    @Autowired
    private OrderProducer orderProducer;

    @GetMapping("/test")
    public String test() {
        String orderId = "1234";
        ProducerMessage producerMessage = new ProducerMessage(
                "mytest",
                "A",
                "hello,张勇".getBytes()
        );
        orderProducer.send(producerMessage, orderId);
        return "hello , first mq message !";
    }

}
