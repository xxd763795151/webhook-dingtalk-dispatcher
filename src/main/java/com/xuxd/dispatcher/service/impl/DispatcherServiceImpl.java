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
import org.apache.commons.collections.CollectionUtils;
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

        List<AlarmConfigDO> configDOS = alarmConfigMapper.selectList(new QueryWrapper<>());

        if (args.containsKey("dingtalk")) {
            // 通过dingtalk过来的，数据不要处理，透传
            configDOS.forEach(config -> {
                if (config.isEnable()) {
                    dispatcherExecutor.executeStandardDingBodyAsync(args, body, config.getUrl(), config.getSecret(),
                            config.isEnableFilter(), FilterType.valueOf(config.getFilterType().toUpperCase(Locale.ROOT)), config.getKeys().split(","));
                }
            });
            return DingResponse.def();
        }
        // 下面是对接原生的alert manager.

        AlertMessage alertMessage = null;
        try {
            alertMessage = new Gson().fromJson(body, AlertMessage.class);
        } catch (Exception e) {
            log.error("Parse alert message failed : {}", body, e);
            return DingResponse.def();
        }

        Map<String, Object> defaultLabels = new HashMap<>();
        if (alertMessage.getGroupLabels() != null) {
            defaultLabels.putAll(alertMessage.getGroupLabels());
        }
        if (alertMessage.getCommonAnnotations() != null) {

            defaultLabels.putAll(alertMessage.getCommonAnnotations());
        }
        if (alertMessage.getCommonLabels() != null) {

            defaultLabels.putAll(alertMessage.getCommonLabels());
        }

        // ding ding
//        if (defaultLabels.containsKey(DingMessageKeys.MSG_TYPE)) {
//            String type = (String) defaultLabels.get(DingMessageKeys.MSG_TYPE);
//            String title = (String) defaultLabels.get(DingMessageKeys.TITLE);
//            String content = (String) defaultLabels.get(DingMessageKeys.CONTENT);
//            Map<String, Object> params = new HashMap<>();
//            params.put(DingMessageKeys.MSG_TYPE, type);
//            Map<String, Object> contentMap = new HashMap<>();
//            contentMap.put(DingMessageKeys.TITLE, title);
//            contentMap.put(DingMessageKeys.CONTENT, content);
//            params.put(type, contentMap);
//            configDOS.forEach(config -> {
//                if (config.isEnable()) {
//                    dispatcherExecutor.executeFromDingTalkAsync(args, new Gson().toJson(params), config.getUrl(), config.getSecret(),
//                            config.isEnableFilter(), FilterType.valueOf(config.getFilterType().toUpperCase(Locale.ROOT)), config.getKeys().split(","));
//                }
//            });
//        }


        List<AlarmConfigDO> alarmConfigDOS = alarmConfigMapper.selectList(null);
        // custom sms
        List<SmsAlarmConfigDO> smsAlarmConfigDOS = smsAlarmConfigMapper.selectList(null);

        List<AlertMessage.AlertsDTO> alerts = alertMessage.getAlerts();
        Set<Map<String, Object>> exist = new HashSet<>();
        if (CollectionUtils.isNotEmpty(alerts)) {
            alerts.forEach(alertsDTO -> {
                Map<String, Object> labels = new HashMap<>(defaultLabels);
                labels.put(Label.STATUS, ConvertUtil.statusOf(alertsDTO.getStatus()));
                labels.put(Label.START_TIME, ConvertUtil.utc2Gmt8(alertsDTO.getStartsAt()));
                labels.put(Label.END_TIME, ConvertUtil.utc2Gmt8(alertsDTO.getEndsAt()));
                if (alertsDTO.getAnnotations() != null) {
                    labels.putAll(alertsDTO.getAnnotations());
                }
                if (alertsDTO.getLabels() != null) {
                    labels.putAll(alertsDTO.getLabels());
                }
                if (exist.contains(labels)) {
                    return;
                }
                exist.add(labels);
                sendAlarm(smsAlarmConfigDOS, labels);
                sendAlarmDing(alarmConfigDOS, labels);
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
            sendAlarmDing(alarmConfigDOS, labels);
        }


        return DingResponse.def();
    }

    private void sendAlarm(List<SmsAlarmConfigDO> smsAlarmConfigDOS, Map<String, Object> labels) {
        smsAlarmConfigDOS.forEach(config -> {
            if (config.isEnable()) {
                String messageBody = ConvertUtil.convert(config.getTemplate(), labels);
                Set<String> set = Arrays
                        .stream(config.getMobile().split(","))
                        .map(String::trim)
                        .filter(StringUtils::isNotEmpty)
                        .collect(Collectors.toSet());
                dispatcherExecutor.executeFromAlertAsync(new ArrayList<>(set), messageBody,
                        config.getType(), config.getEnableFilter(),
                        FilterType.valueOf(config.getFilterType().toUpperCase(Locale.ROOT))
                        , config.getKeys());
            }
        });
    }

    private void sendAlarmDing(List<AlarmConfigDO> alarmConfigDOS, Map<String, Object> labels) {
        alarmConfigDOS.forEach(config -> {
            if (config.isEnable()) {
                String messageBody = ConvertUtil.convert(config.getTemplate(), labels);
                String body = formatMarkdownBody(messageBody, labels);
                Map<String, Object> args = new HashMap<>();
                dispatcherExecutor.executeStandardDingBodyAsync(args, body,
                        config.getUrl(), config.getSecret(), config.isEnableFilter(),
                        FilterType.valueOf(config.getFilterType().toUpperCase(Locale.ROOT))
                        , config.getKeys());
            }
        });
    }


    public static interface Label {
        String STATUS = "status";

        String START_TIME = "startsAt";

        String END_TIME = "endsAt";
    }

    private String formatMarkdownBody(String body, Map<String, Object> labels) {
        Map<String, Object> map = new HashMap<>();
        String alertName = "告警";
        if (labels.containsKey("alertname")) {
            alertName = (String) labels.get("alertname");
        }
        String text = body;
        if (!text.contains("  \n  ")) {
            text = body.replace("\n", "  \n  ");
        }
        map.put("msgtype", "markdown");
        Map<String, Object> content = new HashMap<>();
        content.put("title", alertName);
        content.put("text", text);
        map.put("markdown", content);
        return new Gson().toJson(map);
    }
}
