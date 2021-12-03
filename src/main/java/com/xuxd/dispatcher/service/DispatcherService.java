package com.xuxd.dispatcher.service;

import com.xuxd.dispatcher.beans.DingResponse;
import java.util.Map;

/**
 * webhook-dingtalk-dispatcher.
 *
 * @author xuxd
 * @date 2021-12-02 14:02:20
 **/
public interface DispatcherService {

    DingResponse dispatch(Map<String, Object> args, String body);
}
