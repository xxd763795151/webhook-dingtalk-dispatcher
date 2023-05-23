package com.xuxd.dispatcher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.xuxd.dispatcher.beans.AlertMessage;
import com.xuxd.dispatcher.beans.AlertStatus;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
        Map<String, Object> defaultLabels = new HashMap<>();
        defaultLabels.putAll(alertMessage.getGroupLabels());
        defaultLabels.putAll(alertMessage.getCommonAnnotations());
        defaultLabels.putAll(alertMessage.getCommonLabels());

        List<AlertMessage.AlertsDTO> alerts = alertMessage.getAlerts();
        if (CollectionUtils.isNotEmpty(alerts)) {
            alerts.forEach(alertsDTO -> {
                Map<String, Object> labels = new HashMap<>(defaultLabels);
                labels.put(Label.STATUS, ConvertUtil.statusOf(alertsDTO.getStatus()));
                labels.put(Label.START_TIME, ConvertUtil.utc2Gmt8(alertsDTO.getStartsAt()));
                labels.put(Label.END_TIME, ConvertUtil.utc2Gmt8(alertsDTO.getEndsAt()));
                sendAlarm(smsAlarmConfigDOS, labels);
            });
        } else {
            Map<String, Object> labels = new HashMap<>(defaultLabels);

            if (labels.containsKey(Label.START_TIME)) {
                labels.put(Label.START_TIME, ConvertUtil.utc2Gmt8((String) labels.get(Label.START_TIME)));
            }
            if (labels.containsKey(Label.END_TIME)) {
                labels.put(Label.END_TIME, ConvertUtil.utc2Gmt8((String) labels.get(Label.END_TIME)));
            }
            labels.put(Label.STATUS, ConvertUtil.statusOf(alertMessage.getStatus()));
            sendAlarm(smsAlarmConfigDOS, labels);
        }


        return DingResponse.def();
    }

    private void sendAlarm(List<SmsAlarmConfigDO> smsAlarmConfigDOS, Map<String, Object> labels) {
        smsAlarmConfigDOS.forEach(config -> {
            if (config.isEnable()) {
                String messageBody = ConvertUtil.convert(config.getTemplate(), labels);
                Set<String> set = Arrays.stream(config.getMobile().split(",")).map(String::trim).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
                dispatcherExecutor.executeSmsAsync(new ArrayList<>(set), messageBody, config.getType());
            }
        });
    }


    public static interface Label {
        String STATUS = "status";

        String START_TIME = "startsAt";

        String END_TIME = "endsAt";
    }
}
