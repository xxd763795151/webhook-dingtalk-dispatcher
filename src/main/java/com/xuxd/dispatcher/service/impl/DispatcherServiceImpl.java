package com.xuxd.dispatcher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.xuxd.dispatcher.beans.AlertMessage;
import com.xuxd.dispatcher.beans.DingResponse;
import com.xuxd.dispatcher.beans.dos.AlarmConfigDO;
import com.xuxd.dispatcher.beans.dos.SmsAlarmConfigDO;
import com.xuxd.dispatcher.common.DispatcherExecutor;
import com.xuxd.dispatcher.common.FilterType;
import com.xuxd.dispatcher.dao.AlarmConfigMapper;
import com.xuxd.dispatcher.dao.SmsAlarmConfigMapper;
import com.xuxd.dispatcher.service.DispatcherService;
import com.xuxd.dispatcher.utils.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * webhook-dingtalk-dispatcher.
 *
 * @author xuxd
 * @date 2021-12-02 14:06:05
 **/
@Slf4j
@Service
public class DispatcherServiceImpl implements DispatcherService {

    private final AlarmConfigMapper alarmConfigMapper;

    private final SmsAlarmConfigMapper smsAlarmConfigMapper;

    private final DispatcherExecutor dispatcherExecutor;


    public DispatcherServiceImpl(
            ObjectProvider<AlarmConfigMapper> alarmConfigMapper,
            SmsAlarmConfigMapper smsAlarmConfigMapper,
            ObjectProvider<DispatcherExecutor> dispatcherExecutors) {
        this.alarmConfigMapper = alarmConfigMapper.getIfAvailable();
        this.smsAlarmConfigMapper = smsAlarmConfigMapper;
        this.dispatcherExecutor = dispatcherExecutors.getIfAvailable();
    }

    @Override
    public DingResponse dispatch(Map<String, Object> args, String body) {

        // ding ding
        List<AlarmConfigDO> configDOS = alarmConfigMapper.selectList(new QueryWrapper<>());

        configDOS.forEach(config -> {
            if (config.isEnable()) {
                dispatcherExecutor.executeAsync(args, body, config.getUrl(), config.getSecret(),
                        config.isEnableFilter(), FilterType.valueOf(config.getFilterType().toUpperCase(Locale.ROOT)), config.getKeys().split(","));
            }
        });

        // custom sms
        List<SmsAlarmConfigDO> smsAlarmConfigDOS = smsAlarmConfigMapper.selectList(null);

        AlertMessage alertMessage = null;
        try {
            alertMessage = new Gson().fromJson(body, AlertMessage.class);
        } catch (Exception e) {
            log.error("Parse alert message failed : {}", body, e);
            return DingResponse.def();
        }
        Map<String, Object> labels = new HashMap<>();
        labels.putAll(alertMessage.getGroupLabels());
        labels.putAll(alertMessage.getCommonAnnotations());
        labels.putAll(alertMessage.getCommonLabels());

        smsAlarmConfigDOS.forEach(config -> {
            if (config.isEnable()) {
                String messageBody = ConvertUtil.convert(config.getTemplate(), labels);
                Set<String> set = Arrays.stream(config.getMobile().split(",")).map(String::trim).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
                dispatcherExecutor.executeSmsAsync(new ArrayList<>(set), messageBody);
            }
        });
        return DingResponse.def();
    }


}
