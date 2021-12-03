package com.xuxd.dispatcher;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@MapperScan("com.xuxd.dispatcher.dao")
@SpringBootApplication
public class WebhookDingtalkDispatcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebhookDingtalkDispatcherApplication.class, args);
    }

}
