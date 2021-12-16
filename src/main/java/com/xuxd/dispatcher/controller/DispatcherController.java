package com.xuxd.dispatcher.controller;

import com.xuxd.dispatcher.beans.DingResponse;
import com.xuxd.dispatcher.service.DispatcherService;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * webhook-dingtalk-dispatcher.
 *
 * @author xuxd
 * @date 2021-12-01 15:11:22
 **/
@Slf4j
@RestController
@RequestMapping("/dispatcher")
public class DispatcherController {

    @Autowired
    private DispatcherService dispatcherService;

    @Value("${access-token}")
    private String accessToken;

    @PostMapping
    public Object dispatch(HttpServletRequest request, @RequestBody String body) {
        Enumeration<String> names = request.getParameterNames();
        Map<String, Object> args = new HashMap<>();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name);
            args.put(name, value);
        }
        if (StringUtils.isNotBlank(accessToken)) {
            if (!accessToken.equals(args.get("access_token"))) {
                DingResponse response = new DingResponse();
                response.setErrcode(-9999);
                response.setErrmsg("dispatcher token is invalid");
                log.error("dispatcher token is invalid: [{}]", args);
                return response;
            }
        }
        return dispatcherService.dispatch(args, body);
    }
}
