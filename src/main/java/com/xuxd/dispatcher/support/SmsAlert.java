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

        } catch (Exception e) {
            log.error("Send failed, mobile: {}, message: {}, type: {}", mobile, message, type, e);
        }

    }
}
