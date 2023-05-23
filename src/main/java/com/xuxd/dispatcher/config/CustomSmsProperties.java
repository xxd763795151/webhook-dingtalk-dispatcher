package com.xuxd.dispatcher.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: xuxd
 * @date: 2023/5/22 17:36
 **/
@Data
@Configuration
@ConfigurationProperties("sms")
public class CustomSmsProperties {

    private User smsUser;

    private User voiceUser;

    @Data
    public static class User {

        private int userId;

        private String password;

        private String url;
    }
}
