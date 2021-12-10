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
}
