package com.xuxd.dispatcher.support;

import com.xuxd.dispatcher.beans.SmsType;
import com.xuxd.dispatcher.common.RestClient;
import com.xuxd.dispatcher.config.CustomSmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: xuxd
 * @date: 2023/5/22 17:44
 **/
@Slf4j
@Component
public class SmsAlert {

    private final CustomSmsProperties smsProperties;

    private final RestClient restClient;

    public SmsAlert(CustomSmsProperties smsProperties, RestClient restClient) {
        this.smsProperties = smsProperties;
        this.restClient = restClient;
    }

    public void send(String mobile, String message, int type) {
        try {
            Integer userId = null;
            String password = "";
            String url = "";
            switch (type) {
                case SmsType.SMS:
                    userId = smsProperties.getSmsUser().getUserId();
                    password = smsProperties.getSmsUser().getPassword();
                    url = smsProperties.getSmsUser().getUrl();
                    break;
                case SmsType.VOICE:
                    userId = smsProperties.getVoiceUser().getUserId();
                    password = smsProperties.getVoiceUser().getPassword();
                    url = smsProperties.getVoiceUser().getUrl();
                    break;
            }
            Map<String, String> params = new HashMap<>();
            // TODO: 在这里增加对应的告警参数配置
            String response = restClient.postForm(url, params, String.class);
            log.info("Send complete, response: {}", response);
        } catch (Exception e) {
            log.error("Send failed, mobile: {}, message: {}, type: {}", mobile, message, type, e);
        }

    }
}
