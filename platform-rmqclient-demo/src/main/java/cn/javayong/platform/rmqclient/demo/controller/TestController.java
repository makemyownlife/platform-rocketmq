package cn.javayong.platform.rmqclient.demo.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/hello")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "hello , first short message !";
    }

}
