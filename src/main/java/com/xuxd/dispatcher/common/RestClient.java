package com.xuxd.dispatcher.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

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

    public <T> T postForm(String url, Map<String, String> params, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        params.forEach((k, v) -> map.add(k, v));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        return restTemplate.postForObject(url, request, responseType);
    }
}
