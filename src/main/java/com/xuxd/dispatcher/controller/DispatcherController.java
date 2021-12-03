package com.xuxd.dispatcher.controller;

import com.xuxd.dispatcher.service.DispatcherService;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("/dispatcher")
public class DispatcherController {

    @Autowired
    private DispatcherService dispatcherService;

    @PostMapping
    public Object dispatch(HttpServletRequest request, @RequestBody String body) {
        Enumeration<String> names = request.getParameterNames();
        Map<String, Object> args = new HashMap<>();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name);
            args.put(name, value);
        }
        return dispatcherService.dispatch(args, body);
    }

    // 签名：SEC8b9e1c2eb7f21821d13f56ce804dca874663fae08021a411dd6bf6679391ba5e
    // https://oapi.dingtalk.com/robot/send?access_token=6a940d0b7ebdf4d0e7e08cf76803ac24a1d81430ad1ec83de24039ac8667df46

    // https://oapi.dingtalk.com/robot/send?access_token=a5750cc9b16c0505b4b072ede275b99a8067c1cf5eb8bbc1e5988f34aca0aab4

    // https://oapi.dingtalk.com/robot/send?access_token=ca65878b8a86d790167cd5eea8648d3226761c062b4b81b3266012b06d97d406
}
