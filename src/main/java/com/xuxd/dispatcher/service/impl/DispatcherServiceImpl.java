package com.xuxd.dispatcher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuxd.dispatcher.beans.DingResponse;
import com.xuxd.dispatcher.beans.dos.AlarmConfigDO;
import com.xuxd.dispatcher.common.DispatcherExecutor;
import com.xuxd.dispatcher.dao.AlarmConfigMapper;
import com.xuxd.dispatcher.service.DispatcherService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

/**
 * webhook-dingtalk-dispatcher.
 *
 * @author xuxd
 * @date 2021-12-02 14:06:05
 **/
@Service
public class DispatcherServiceImpl implements DispatcherService {

    private final AlarmConfigMapper alarmConfigMapper;

    private final DispatcherExecutor dispatcherExecutor;

    public DispatcherServiceImpl(
        ObjectProvider<AlarmConfigMapper> alarmConfigMapper, ObjectProvider<DispatcherExecutor> dispatcherExecutors) {
        this.alarmConfigMapper = alarmConfigMapper.getIfAvailable();
        this.dispatcherExecutor = dispatcherExecutors.getIfAvailable();
    }

    @Override public DingResponse dispatch(Map<String, Object> args, String body) {

        List<AlarmConfigDO> configDOS = alarmConfigMapper.selectList(new QueryWrapper<>());

        configDOS.forEach(config -> {
            if (config.isEnable()) {
                dispatcherExecutor.executeAsync(args, body, config.getUrl(), config.getSecret(), config.isEnableFilter(), config.getKeys().split(","));
            }
        });
        return DingResponse.def();
    }

}
