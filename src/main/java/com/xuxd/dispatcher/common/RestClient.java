package com.xuxd.dispatcher.common;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * webhook-dingtalk-dispatcher.
 *
 * @author xuxd
 * @date 2021-12-02 14:08:50
 **/
@Component
public class RestClient {

    @Autowired
    private RestTemplate restTemplate;

    public <T> T post(String url, Object body, Class<T> responseType,
        Map<String, ?> uriVariables) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(body, headers);

        return restTemplate.postForObject(url, entity, responseType, uriVariables);
    }
}
