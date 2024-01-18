package cn.javayong.platform.rmqclient.demo.controller;

import cn.javayong.platform.rmqclient.core.Producer;
import cn.javayong.platform.rmqclient.core.ProducerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class TestController {

    @Autowired
    private Producer producer;

    @GetMapping("/test")
    public String test() {
        ProducerMessage producerMessage = new ProducerMessage(
                "mytest",
                "A",
                "hello,张勇".getBytes()
        );
        producer.send(producerMessage);
        return "hello , first mq message !";
    }

}
